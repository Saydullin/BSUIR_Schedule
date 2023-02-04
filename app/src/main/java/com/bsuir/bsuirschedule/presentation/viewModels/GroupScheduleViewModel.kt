package com.bsuir.bsuirschedule.presentation.viewModels

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsuir.bsuirschedule.BuildConfig
import com.bsuir.bsuirschedule.domain.models.*
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.domain.usecase.GetCurrentWeekUseCase
import com.bsuir.bsuirschedule.domain.usecase.GetSavedScheduleUseCase
import com.bsuir.bsuirschedule.domain.usecase.SharedPrefsUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.*
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.presentation.widgets.ScheduleWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupScheduleViewModel(
    private val getScheduleUseCase: GetScheduleUseCase,
    private val scheduleSubjectUseCase: ScheduleSubjectUseCase,
    private val updateScheduleSettingsUseCase: UpdateScheduleSettingsUseCase,
    private val savedScheduleUseCase: GetSavedScheduleUseCase,
    private val saveScheduleUseCase: SaveScheduleUseCase,
    private val deleteScheduleUseCase: DeleteScheduleUseCase,
    private val sharedPrefsUseCase: SharedPrefsUseCase,
    private val getCurrentWeekUseCase: GetCurrentWeekUseCase
) : ViewModel() {

    private val loading = MutableLiveData(false)
    private val update = MutableLiveData(false)
    private val dataLoading = MutableLiveData(true)
    private val scheduleLoaded = MutableLiveData<SavedSchedule>(null)
    private val activeSubject = MutableLiveData<ScheduleSubject>(null)
    private val groupLoading = MutableLiveData(false)
    private val settingsUpdated = MutableLiveData(false)
    private val employeeLoading = MutableLiveData(false)
    private val activeSchedule = MutableLiveData<SavedSchedule>(null)
    private val schedule = MutableLiveData<Schedule>(null)
    private val deletedSchedule = MutableLiveData<SavedSchedule>(null)
    private val examsSchedule = MutableLiveData<Schedule>(null)
    private val error = MutableLiveData<StateStatus>(null)
    private val success = MutableLiveData<Int>(null)
    val scheduleStatus = schedule
    val activeSubjectStatus = activeSubject
    val settingsUpdatedStatus = settingsUpdated
    val deletedScheduleStatus = deletedSchedule
    val examsScheduleStatus = examsSchedule
    val errorStatus = error
    val successStatus = success
    val updateStatus = update
    val scheduleLoadedStatus = scheduleLoaded
    val loadingStatus = loading
    val dataLoadingStatus = dataLoading
    val groupLoadingStatus = groupLoading
    val employeeLoadingStatus = employeeLoading

    init {
        if (sharedPrefsUseCase.getScheduleCounter() < BuildConfig.SCHEDULES_UPDATE_COUNTER) {
            sharedPrefsUseCase.setScheduleCounter(BuildConfig.SCHEDULES_UPDATE_COUNTER)
            updateAllSchedules()
        }
    }

    fun setScheduleLoadedNull() {
        scheduleLoaded.value = null
    }

    fun setSuccessNull() {
        success.value = null
    }

    fun setActiveSubject(subject: ScheduleSubject) {
        activeSubject.value = subject
    }

    fun setDeletedScheduleNull() {
        deletedSchedule.value = null
    }

    fun setUpdateStatus(updateStatus: Boolean) {
        update.value = updateStatus
    }

    fun updateSchedule() {
        if (updateStatus.value == true) {
            val scheduleId = schedule.value?.id ?: return
            settingsUpdated.value = false
            getScheduleById(scheduleId, false)
        }
    }

    fun getActiveSubject(): ScheduleSubject? {
        return activeSubject.value
    }

    fun getActiveSchedule(): Schedule? {
        return schedule.value
    }

    fun ignoreSubject(scheduleSubject: ScheduleSubject, isIgnored: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (schedule.value == null || schedule.value!!.id == -1) {
                error.postValue(StateStatus(
                    state = StateStatus.ERROR_STATE,
                    type = Resource.DATA_ERROR,
                ))
            }

            val scheduleId = schedule.value!!.id
            when (
                val result = scheduleSubjectUseCase.ignore(
                    scheduleId = scheduleId,
                    scheduleSubject = scheduleSubject,
                    isIgnored = isIgnored
                )
            ) {
                is Resource.Success -> {
                    val data = result.data!!
                    when (
                        val saveResponse = saveScheduleUseCase.invoke(data)
                    ) {
                        is Resource.Success -> {
                            if (isIgnored) {
                                success.postValue(Resource.SCHEDULE_SUBJECT_IGNORED)
                            } else {
                                success.postValue(Resource.SCHEDULE_SUBJECT_NOT_IGNORED)
                            }
                            getScheduleById(data.id, isNotUpdate = false)
                        }
                        is Resource.Error -> {
                            error.postValue(StateStatus(
                                state = StateStatus.ERROR_STATE,
                                type = saveResponse.errorType,
                            ))
                        }
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.errorType,
                    ))
                }
            }
        }
    }

    fun editSubject(scheduleSubject: ScheduleSubject, changeSettings: ChangeSubjectSettings) {
        viewModelScope.launch(Dispatchers.IO) {
            if (schedule.value == null || schedule.value!!.id == -1) {
                error.postValue(StateStatus(
                    state = StateStatus.ERROR_STATE,
                    type = Resource.DATA_ERROR,
                ))
            }

            val scheduleId = schedule.value!!.id
            when (
                val result = scheduleSubjectUseCase.edit(
                    scheduleId = scheduleId,
                    scheduleSubject = scheduleSubject,
                    deleteSettings = changeSettings
                )
            ) {
                is Resource.Success -> {
                    val data = result.data!!
                    when (
                        val saveResponse = saveScheduleUseCase.invoke(data)
                    ) {
                        is Resource.Success -> {
                            success.postValue(Resource.SCHEDULE_SUBJECT_EDITED)
                            getScheduleById(data.id, isNotUpdate = false)
                        }
                        is Resource.Error -> {
                            error.postValue(StateStatus(
                                state = StateStatus.ERROR_STATE,
                                type = saveResponse.errorType,
                            ))
                        }
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.errorType,
                    ))
                }
            }
        }
    }

    fun deleteSubject(scheduleSubject: ScheduleSubject, changeSettings: ChangeSubjectSettings) {
        viewModelScope.launch(Dispatchers.IO) {
            if (schedule.value == null || schedule.value!!.id == -1) {
                error.postValue(StateStatus(
                    state = StateStatus.ERROR_STATE,
                    type = Resource.DATA_ERROR,
                ))
            }

            val scheduleId = schedule.value!!.id
            when (
                val result = scheduleSubjectUseCase.delete(
                    scheduleId = scheduleId,
                    scheduleSubject = scheduleSubject,
                    deleteSettings = changeSettings
                )
            ) {
                is Resource.Success -> {
                    val data = result.data!!
                    when (
                        val saveResponse = saveScheduleUseCase.invoke(data)
                    ) {
                        is Resource.Success -> {
                            success.postValue(Resource.SCHEDULE_SUBJECT_DELETED)
                            getScheduleById(data.id, isNotUpdate = false)
                        }
                        is Resource.Error -> {
                            error.postValue(StateStatus(
                                state = StateStatus.ERROR_STATE,
                                type = saveResponse.errorType,
                            ))
                        }
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.errorType,
                    ))
                }
            }
        }
    }

    fun getOrUploadSchedule(savedSchedule: SavedSchedule) {
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val result = getScheduleUseCase.getById(savedSchedule.id, 0, -1)
            ) {
                is Resource.Success -> {
                    val data = result.data!!
                    saveScheduleToLiveData(data)
                }
                is Resource.Error -> {
                    if (savedSchedule.isGroup) {
                        getGroupScheduleAPI(savedSchedule.group)
                    } else {
                        getEmployeeScheduleAPI(savedSchedule.employee)
                    }
                }
            }
        }
    }

    fun selectSchedule(savedSchedule: SavedSchedule) {
        viewModelScope.launch(Dispatchers.IO) {
            if (savedSchedule.id == activeSchedule.value?.id &&
                schedule.value?.isNotEmpty() == true) {
                return@launch
            }
            getScheduleById(savedSchedule.id)
        }
    }

    fun closeError() {
        error.value = null
    }

    fun initActiveSchedule() {
        viewModelScope.launch(Dispatchers.IO) {
            val activeScheduleId = sharedPrefsUseCase.getActiveScheduleId()
            if (activeScheduleId != -1) {
                getScheduleById(activeScheduleId)
            }
        }
    }

    fun renameSchedule(scheduleId: Int, newTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val result = getScheduleUseCase.getById(scheduleId, 0, -1)
            ) {
                is Resource.Success -> {
                    val data = result.data!!
                    if (data.isGroup()) {
                        data.group.title = newTitle
                    } else {
                        data.employee.title = newTitle
                    }
                    saveGroupSchedule(data)
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.errorType,
                        message = result.message
                    ))
                }
            }
        }
    }

    fun getGroupScheduleAPI(group: Group, isUpdate: Boolean = false, toNotify: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            groupLoading.postValue(true)
            val currentWeek = getCurrentWeekUseCase.getCurrentWeek()
            if (currentWeek is Resource.Error) {
                val currentWeekAPI = launch(Dispatchers.IO) {
                    getCurrentWeekUseCase.updateWeekNumber()
                }
                currentWeekAPI.join()
            }
            when (
                val groupScheduleResponse = getScheduleUseCase.getGroupAPI(group.name)
            ) {
                is Resource.Success -> {
                    val groupSchedule = groupScheduleResponse.data!!
                    saveGroupSchedule(groupSchedule)
                    scheduleLoaded.postValue(group.toSavedSchedule(!groupSchedule.isNotExistExams()))
                    if (toNotify) {
                        if (isUpdate) {
                            success.postValue(Resource.SCHEDULE_UPDATED_SUCCESS)
                        } else {
                            success.postValue(Resource.SCHEDULE_LOADED_SUCCESS)
                        }
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = groupScheduleResponse.errorType,
                        message = groupScheduleResponse.message
                    ))
                }
            }
            loading.postValue(false)
            groupLoading.postValue(false)
        }
    }

    fun getEmployeeScheduleAPI(employee: Employee, isUpdate: Boolean = false, toNotify: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            employeeLoading.postValue(true)
            when (
                val groupSchedule = getScheduleUseCase.getEmployeeAPI(employee.urlId)
            ) {
                is Resource.Success -> {
                    val data = groupSchedule.data!!
                    saveGroupSchedule(data)
                    scheduleLoaded.postValue(employee.toSavedSchedule(!data.isNotExistExams()))
                    if (toNotify) {
                        if (isUpdate) {
                            success.postValue(Resource.SCHEDULE_UPDATED_SUCCESS)
                        } else {
                            success.postValue(Resource.SCHEDULE_LOADED_SUCCESS)
                        }
                    }
                }
                is Resource.Error -> {
                    error.postValue(
                        StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = groupSchedule.errorType,
                            message = groupSchedule.message
                        )
                    )
                }
            }
            loading.postValue(false)
            employeeLoading.postValue(false)
        }
    }

    private fun saveGroupSchedule(groupSchedule: Schedule) {
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val saveResponse = saveScheduleUseCase.invoke(groupSchedule)
            ) {
                is Resource.Success -> {
                    if (groupSchedule.id == -1) {
                        error.postValue(StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = Resource.DATA_ERROR
                        ))
                        schedule.postValue(null)
                    } else {
                        getScheduleById(groupSchedule.id)
                    }
                }
                is Resource.Error -> {
                    schedule.postValue(null)
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = saveResponse.errorType,
                        message = saveResponse.message
                    ))
                }
            }
        }
    }

    fun updateScheduleSettings(id: Int, newSettings: ScheduleSettings) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsUpdated.postValue(false)
            when (
                val saveResponse = updateScheduleSettingsUseCase.invoke(id, newSettings)
            ) {
                is Resource.Success -> {
                    settingsUpdated.postValue(true)
                    getScheduleById(id)
                }
                is Resource.Error -> {
                    schedule.postValue(null)
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = saveResponse.errorType,
                        message = saveResponse.message
                    ))
                }
            }
        }
    }

    private fun saveScheduleToLiveData(scheduleData: Schedule) {
        viewModelScope.launch(Dispatchers.IO) {
            activeSchedule.postValue(scheduleData.toSavedSchedule())
            schedule.postValue(scheduleData)
            if (!scheduleData.isExamsNotExist()) {
                examsSchedule.postValue(scheduleData)
            } else {
                examsSchedule.postValue(null)
            }
            sharedPrefsUseCase.setActiveScheduleId(scheduleData.id)
        }
    }

    private fun getScheduleById(scheduleId: Int, isNotUpdate: Boolean = true) {
        if (scheduleId == -1) {
            schedule.postValue(null)
            error.postValue(StateStatus(
                state = StateStatus.ERROR_STATE,
                type = Resource.DATABASE_NOT_FOUND_ERROR
            ))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            dataLoading.postValue(isNotUpdate)
            try {
                when (
                    val result = getScheduleUseCase.getById(scheduleId, 0, -1)
                ) {
                    is Resource.Success -> {
                        val data = result.data!!
                        saveScheduleToLiveData(data)
                    }
                    is Resource.Error -> {
                        schedule.postValue(null)
                        error.postValue(StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = result.errorType,
                            message = result.message
                        ))
                    }
                }
            } catch (e: Exception) {
                schedule.postValue(null)
                error.postValue(StateStatus(
                    state = StateStatus.ERROR_STATE,
                    type = Resource.UNKNOWN_ERROR,
                    message = e.message
                ))
            }
            dataLoading.postValue(false)
        }
    }

    fun deleteSchedule(savedSchedule: SavedSchedule) {
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val isDeletedSchedule = deleteScheduleUseCase.invoke(savedSchedule)
            ) {
                is Resource.Success -> {
                    success.postValue(Resource.SCHEDULE_DELETED_SUCCESS)
                    deletedSchedule.postValue(savedSchedule)
                    if (schedule.value?.id == savedSchedule.id) {
                        schedule.postValue(null)
                        activeSchedule.postValue(null)
                        sharedPrefsUseCase.setActiveScheduleId(-1)
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = isDeletedSchedule.errorType,
                        message = isDeletedSchedule.message
                    ))
                }
            }
        }
    }

    private fun updateAllSchedules() {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            when (
                val savedSchedules = savedScheduleUseCase.getSavedSchedules()
            ) {
                is Resource.Success -> {
                    val savedSchedulesList = savedSchedules.data ?: arrayListOf()
                    val updateAllSchedulesIO = launch(Dispatchers.IO) {
                        savedSchedulesList.forEach { savedSchedule ->
                            if (savedSchedule.isGroup) {
                                getGroupScheduleAPI(savedSchedule.group, isUpdate = true, toNotify = false)
                            } else {
                                getEmployeeScheduleAPI(savedSchedule.employee, isUpdate = true, toNotify = false)
                            }
                        }
                    }
                    updateAllSchedulesIO.join()
                    if (updateAllSchedulesIO.isCancelled) {
                        success.postValue(Resource.UPDATE_ERROR)
                    } else {
                        success.postValue(Resource.ALL_SCHEDULES_UPDATED_SUCCESS)
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = savedSchedules.errorType,
                        message = savedSchedules.message
                    ))
                }
            }
            loading.postValue(false)
        }
    }

}


