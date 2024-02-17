package com.bsuir.bsuirschedule.presentation.viewModels

import android.util.Log
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
import com.bsuir.bsuirschedule.domain.utils.StatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val getScheduleUseCase: GetScheduleUseCase,
    private val scheduleSubjectUseCase: ScheduleSubjectUseCase,
    private val updateScheduleSettingsUseCase: UpdateScheduleSettingsUseCase,
    private val savedScheduleUseCase: GetSavedScheduleUseCase,
    private val saveScheduleUseCase: SaveScheduleUseCase,
    private val deleteScheduleUseCase: DeleteScheduleUseCase,
    private val sharedPrefsUseCase: SharedPrefsUseCase,
    private val getCurrentWeekUseCase: GetCurrentWeekUseCase,
    private val addScheduleSubjectUseCase: AddScheduleSubjectUseCase
) : ViewModel() {

    private val loading = MutableLiveData(false)
    private val update = MutableLiveData(false)
    private val dataLoading = MutableLiveData(true)
    private val scheduleLoaded = MutableLiveData<SavedSchedule>(null)
    private val activeSubject = MutableLiveData<ScheduleSubject>(null)
    private val groupLoading = MutableLiveData(false)
    private val settingsUpdated = MutableLiveData(false)
    private val employeeLoading = MutableLiveData(false)
    private val activeScheduleId = MutableLiveData<Int>(null)
    private val schedule = MutableLiveData<Schedule>(null)
    private val deletedSchedule = MutableLiveData<SavedSchedule>(null)
    private val error = MutableLiveData<StateStatus>(null)
    private val success = MutableLiveData<StatusCode>(null)
    val scheduleStatus = schedule
    val activeSubjectStatus = activeSubject
    val settingsUpdatedStatus = settingsUpdated
    val deletedScheduleStatus = deletedSchedule
    val errorStatus = error
    val successStatus = success
    val scheduleLoadedStatus = scheduleLoaded
    val loadingStatus = loading
    val dataLoadingStatus = dataLoading
    val groupLoadingStatus = groupLoading
    val employeeLoadingStatus = employeeLoading

    init {
        val scheduleId = activeScheduleId.value ?: -1

        if (sharedPrefsUseCase.getScheduleCounter() < BuildConfig.SCHEDULES_UPDATE_COUNTER) {
            sharedPrefsUseCase.setScheduleCounter(BuildConfig.SCHEDULES_UPDATE_COUNTER)
            updateAllSchedules()
        }

        updateSchedule(scheduleId)
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

    fun updateSchedule(scheduleId: Int = activeScheduleId.value ?: -1) {
        if (update.value == true && scheduleId != -1) {
            settingsUpdated.postValue(false)
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
                    type = StatusCode.DATA_ERROR,
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
                        val saveResponse = saveScheduleUseCase.execute(data)
                    ) {
                        is Resource.Success -> {
                            if (isIgnored) {
                                success.postValue(StatusCode.SCHEDULE_SUBJECT_IGNORED)
                            } else {
                                success.postValue(StatusCode.SCHEDULE_SUBJECT_NOT_IGNORED)
                            }
                            getScheduleById(data.id, isNotUpdate = false)
                        }
                        is Resource.Error -> {
                            error.postValue(StateStatus(
                                state = StateStatus.ERROR_STATE,
                                type = saveResponse.statusCode,
                            ))
                        }
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.statusCode,
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
                    type = StatusCode.DATA_ERROR,
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
                        val saveResponse = saveScheduleUseCase.execute(data)
                    ) {
                        is Resource.Success -> {
                            success.postValue(StatusCode.SCHEDULE_SUBJECT_EDITED)
                            getScheduleById(data.id, isNotUpdate = false)
                        }
                        is Resource.Error -> {
                            error.postValue(StateStatus(
                                state = StateStatus.ERROR_STATE,
                                type = saveResponse.statusCode,
                            ))
                        }
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.statusCode,
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
                    type = StatusCode.DATA_ERROR,
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
                        val saveResponse = saveScheduleUseCase.execute(data)
                    ) {
                        is Resource.Success -> {
                            success.postValue(StatusCode.SCHEDULE_SUBJECT_DELETED)
                            getScheduleById(data.id, isNotUpdate = false)
                        }
                        is Resource.Error -> {
                            error.postValue(StateStatus(
                                state = StateStatus.ERROR_STATE,
                                type = saveResponse.statusCode,
                            ))
                        }
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.statusCode,
                    ))
                }
            }
        }
    }

    fun addCustomSubject(subject: ScheduleSubject, sourceItemsText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val scheduleId = schedule.value?.id ?: return@launch
            when (
                val res = addScheduleSubjectUseCase.execute(scheduleId, subject, sourceItemsText)
            ) {
                is Resource.Success -> {
                    success.postValue(StatusCode.SCHEDULE_SUBJECT_ADDED)
                    updateSchedule()
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = res.statusCode,
                        message = res.message
                    ))
                }
            }
        }
    }

    fun getOrUploadSchedule(savedSchedule: SavedSchedule) {
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val result = getScheduleUseCase.getById(savedSchedule.id)
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

    fun selectSchedule(scheduleId: Int) {
        activeScheduleId.value = scheduleId
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
                val result = getScheduleUseCase.getById(scheduleId)
            ) {
                is Resource.Success -> {
                    val data = result.data!!
                    if (data.isGroup()) {
                        data.group.title = newTitle
                    } else {
                        data.employee.title = newTitle
                    }
                    saveSchedule(data)
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.statusCode,
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
            val groupScheduleResponse = getScheduleUseCase.getGroupAPI(group.name)
            if (groupScheduleResponse is Resource.Success) {
                if (groupScheduleResponse.data != null) {
                    val groupSchedule = groupScheduleResponse.data
                    scheduleLoaded.postValue(group.toSavedSchedule(!groupSchedule.isExamsNotExist()))
                    if (toNotify) {
                        saveSchedule(groupSchedule)
                        if (isUpdate) {
                            success.postValue(StatusCode.SCHEDULE_UPDATED_SUCCESS)
                        } else {
                            success.postValue(StatusCode.SCHEDULE_LOADED_SUCCESS)
                        }
                    } else {
                        saveScheduleSilently(groupSchedule)
                    }
                } else {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = StatusCode.DATA_ERROR,
                        message = groupScheduleResponse.message
                    ))
                }
            } else {
                error.postValue(StateStatus(
                    state = StateStatus.ERROR_STATE,
                    type = groupScheduleResponse.statusCode,
                    message = groupScheduleResponse.message
                ))
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
                val groupSchedule = employee.urlId?.let { getScheduleUseCase.getEmployeeAPI(it) }
            ) {
                is Resource.Success -> {
                    val data = groupSchedule.data!!
                    scheduleLoaded.postValue(employee.toSavedSchedule(!data.isExamsNotExist()))
                    if (toNotify) {
                        saveSchedule(data)
                        if (isUpdate) {
                            success.postValue(StatusCode.SCHEDULE_UPDATED_SUCCESS)
                        } else {
                            success.postValue(StatusCode.SCHEDULE_LOADED_SUCCESS)
                        }
                    } else {
                        saveScheduleSilently(data)
                    }
                }
                else -> {
                    error.postValue(
                        StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = groupSchedule?.statusCode ?: StatusCode.UNKNOWN_ERROR,
                            message = groupSchedule?.message ?: ""
                        )
                    )
                }
            }
            loading.postValue(false)
            employeeLoading.postValue(false)
        }
    }

    private fun saveSchedule(groupSchedule: Schedule) {
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val saveResponse = saveScheduleUseCase.execute(groupSchedule)
            ) {
                is Resource.Success -> {
                    if (groupSchedule.id == -1) {
                        error.postValue(StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = StatusCode.DATA_ERROR
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
                        type = saveResponse.statusCode,
                        message = saveResponse.message
                    ))
                }
            }
        }
    }

    private fun saveScheduleSilently(groupSchedule: Schedule) {
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val saveResponse = saveScheduleUseCase.execute(groupSchedule)
            ) {
                is Resource.Success -> {
                    if (groupSchedule.id == -1) {
                        error.postValue(StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = StatusCode.DATA_ERROR
                        ))
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = saveResponse.statusCode,
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
                        type = saveResponse.statusCode,
                        message = saveResponse.message
                    ))
                }
            }
        }
    }

    private fun saveScheduleToLiveData(scheduleData: Schedule) {
        activeScheduleId.postValue(scheduleData.id)
        schedule.postValue(scheduleData)
        sharedPrefsUseCase.setActiveScheduleId(scheduleData.id)
    }

    private fun getScheduleById(scheduleId: Int, isNotUpdate: Boolean = true) {
        if (scheduleId == -1) {
            schedule.postValue(null)
            error.postValue(StateStatus(
                state = StateStatus.ERROR_STATE,
                type = StatusCode.DATABASE_NOT_FOUND_ERROR
            ))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            dataLoading.postValue(isNotUpdate)
            when (
                val result = getScheduleUseCase.getById(scheduleId)
            ) {
                is Resource.Success -> {
                    val data = result.data!!
                    saveScheduleToLiveData(data)
                }
                is Resource.Error -> {
                    schedule.postValue(null)
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.statusCode,
                        message = result.message
                    ))
                }
            }
            settingsUpdated.postValue(false)
            dataLoading.postValue(false)
        }
    }

    fun deleteSchedule(savedSchedule: SavedSchedule) {
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val isDeletedSchedule = deleteScheduleUseCase.invoke(savedSchedule)
            ) {
                is Resource.Success -> {
                    success.postValue(StatusCode.SCHEDULE_DELETED_SUCCESS)
                    deletedSchedule.postValue(savedSchedule)
                    if (schedule.value?.id == savedSchedule.id) {
                        schedule.postValue(null)
                        activeScheduleId.postValue(null)
                        sharedPrefsUseCase.setActiveScheduleId(-1)
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = isDeletedSchedule.statusCode,
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
                            when (
                                val groupScheduleResponse = if (savedSchedule.isGroup)
                                    getScheduleUseCase.getGroupAPI(savedSchedule.group.name)
                                else
                                    savedSchedule.employee.urlId?.let {
                                        getScheduleUseCase.getEmployeeAPI(
                                            it
                                        )
                                    }
                            ) {
                                is Resource.Success -> {
                                    val groupSchedule = groupScheduleResponse.data!!
                                    scheduleLoaded.postValue(savedSchedule)
                                    saveScheduleSilently(groupSchedule)
                                }
                                else -> {
                                    error.postValue(StateStatus(
                                        state = StateStatus.ERROR_STATE,
                                        type = groupScheduleResponse?.statusCode ?: StatusCode.UNKNOWN_ERROR,
                                        message = groupScheduleResponse?.message
                                    ))
                                }
                            }
                        }
                    }
                    updateAllSchedulesIO.join()
                    if (updateAllSchedulesIO.isCancelled) {
                        success.postValue(StatusCode.UPDATE_ERROR)
                    } else {
                        success.postValue(StatusCode.ALL_SCHEDULES_UPDATED_SUCCESS)
                        if (schedule.value != null && schedule.value?.id != -1) {
                            val scheduleId = schedule.value!!.id
                            getScheduleById(scheduleId, false)
                        }
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = savedSchedules.statusCode,
                        message = savedSchedules.message
                    ))
                }
            }
            loading.postValue(false)
        }
    }

}


