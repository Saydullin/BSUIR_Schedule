package com.bsuir.bsuirschedule.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsuir.bsuirschedule.domain.models.*
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.domain.usecase.GetCurrentWeekUseCase
import com.bsuir.bsuirschedule.domain.usecase.SharedPrefsUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.DeleteScheduleUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.GetScheduleUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.SaveScheduleUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.UpdateScheduleSettingsUseCase
import com.bsuir.bsuirschedule.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupScheduleViewModel(
    private val getScheduleUseCase: GetScheduleUseCase,
    private val updateScheduleSettingsUseCase: UpdateScheduleSettingsUseCase,
    private val saveScheduleUseCase: SaveScheduleUseCase,
    private val deleteScheduleUseCase: DeleteScheduleUseCase,
    private val sharedPrefsUseCase: SharedPrefsUseCase,
    private val getCurrentWeekUseCase: GetCurrentWeekUseCase
) : ViewModel() {

    private val loading = MutableLiveData(false)
    private val dataLoading = MutableLiveData(false)
    private val scheduleLoaded = MutableLiveData<SavedSchedule>(null)
    private val groupLoading = MutableLiveData(false)
    private val settingsUpdated = MutableLiveData(false)
    private val employeeLoading = MutableLiveData(false)
    private val activeSchedule = MutableLiveData<SavedSchedule>(null)
    private val schedule = MutableLiveData<Schedule>(null)
    private val deletedSchedule = MutableLiveData<SavedSchedule>(null)
    private val examsSchedule = MutableLiveData<Schedule>(null)
    private val error = MutableLiveData<StateStatus>(null)
    val scheduleStatus = schedule
    val settingsUpdatedStatus = settingsUpdated
    val deletedScheduleStatus = deletedSchedule
    val examsScheduleStatus = examsSchedule
    val errorStatus = error
    val scheduleLoadedStatus = scheduleLoaded
    val loadingStatus = loading
    val dataLoadingStatus = dataLoading
    val groupLoadingStatus = groupLoading
    val employeeLoadingStatus = employeeLoading

    fun setScheduleLoadedNull() {
        scheduleLoaded.value = null
    }

    fun setDeletedScheduleNull() {
        deletedSchedule.value = null
    }

    fun updateSchedule() {
        schedule.value = schedule.value
        settingsUpdated.value = false
    }

    fun getActiveSchedule(): Schedule? {
        return schedule.value
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

    fun getGroupScheduleAPI(group: Group) {
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
                    error.postValue(StateStatus(
                        state = StateStatus.SUCCESS_STATE,
                        type = Resource.SCHEDULE_LOADED_SUCCESS,
                    ))
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

    fun getEmployeeScheduleAPI(employee: Employee) {
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
                    error.postValue(StateStatus(
                        state = StateStatus.SUCCESS_STATE,
                        type = Resource.SCHEDULE_LOADED_SUCCESS,
                    ))
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
                        schedule.postValue(Schedule.empty)
                    } else {
                        getScheduleById(groupSchedule.id)
                    }
                }
                is Resource.Error -> {
                    schedule.postValue(Schedule.empty)
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
                    schedule.postValue(Schedule.empty)
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

    private fun getScheduleById(scheduleId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataLoading.postValue(true)
            try {
                when (
                    val result = getScheduleUseCase.getById(scheduleId, 0, -1)
                ) {
                    is Resource.Success -> {
                        val data = result.data!!
                        saveScheduleToLiveData(data)
                    }
                    is Resource.Error -> {
                        schedule.postValue(Schedule.empty)
                        error.postValue(StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = result.errorType,
                            message = result.message
                        ))
                    }
                }
            } catch (e: Exception) {
                schedule.postValue(Schedule.empty)
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
                    error.postValue(StateStatus(
                        state = StateStatus.SUCCESS_STATE,
                        type = Resource.SCHEDULE_DELETED_SUCCESS
                    ))
                    deletedSchedule.postValue(savedSchedule)
                    if (schedule.value?.id == savedSchedule.id) {
                        schedule.postValue(Schedule.empty)
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

}


