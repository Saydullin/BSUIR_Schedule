package com.example.bsuirschedule.presentation.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bsuirschedule.domain.models.*
import com.example.bsuirschedule.domain.usecase.EmployeeScheduleUseCase
import com.example.bsuirschedule.domain.usecase.FullExamsScheduleUseCase
import com.example.bsuirschedule.domain.usecase.GroupScheduleUseCase
import com.example.bsuirschedule.domain.usecase.SharedPrefsUseCase
import com.example.bsuirschedule.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupScheduleViewModel(
    private val groupScheduleUseCase: GroupScheduleUseCase,
    private val employeeScheduleUseCase: EmployeeScheduleUseCase,
    private val examsScheduleUseCase: FullExamsScheduleUseCase,
    private val sharedPrefsUseCase: SharedPrefsUseCase,
) : ViewModel() {

    private val loading = MutableLiveData(false)
    private val groupLoading = MutableLiveData(false)
    private val employeeLoading = MutableLiveData(false)
    private val activeSchedule = MutableLiveData<SavedSchedule>(null)
    private val schedule = MutableLiveData(Schedule.empty)
    private val examsSchedule = MutableLiveData<Schedule>(null)
    private val error = MutableLiveData<StateStatus>(null)
    val scheduleStatus = schedule
    val examsScheduleStatus = examsSchedule
    val activeScheduleStatus = activeSchedule
    val errorStatus = error
    val loadingStatus = loading
    val groupLoadingStatus = groupLoading
    val employeeLoadingStatus = employeeLoading

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
            when (
                val groupScheduleResponse = groupScheduleUseCase.getGroupScheduleAPI(group.name)
            ) {
                is Resource.Success -> {
                    val groupSchedule = groupScheduleResponse.data!!
                    saveGroupSchedule(groupSchedule)
                    error.postValue(StateStatus(
                        state = StateStatus.SUCCESS_STATE,
                        type = 0,
                    ))
                }
                is Resource.Error -> {
                    schedule.postValue(Schedule.empty)
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
                val groupSchedule = employeeScheduleUseCase.getScheduleAPI(employee.urlId)
            ) {
                is Resource.Success -> {
                    val data = groupSchedule.data!!
                    saveGroupSchedule(data)
                    error.postValue(StateStatus(
                        state = StateStatus.SUCCESS_STATE,
                        type = 0,
                    ))
                }
                is Resource.Error -> {
                    schedule.postValue(Schedule.empty)
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

    private suspend fun saveGroupSchedule(groupSchedule: GroupSchedule) {
        when (
            val saveResponse = groupScheduleUseCase.saveSchedule(groupSchedule)
        ) {
            is Resource.Success -> {
                when (
                    val fullSchedule = groupScheduleUseCase.getFullSchedule(groupSchedule)
                ) {
                    is Resource.Success -> {
                        val scheduleData = fullSchedule.data
                        if (scheduleData?.id == -1) {
                            error.postValue(StateStatus(
                                state = StateStatus.ERROR_STATE,
                                type = Resource.DATA_ERROR
                            ))
                            schedule.postValue(Schedule.empty)
                        } else {
                            getScheduleById(scheduleData?.id ?: -1)
                        }
                    }
                    is Resource.Error -> {
                        schedule.postValue(Schedule.empty)
                        error.postValue(StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = fullSchedule.errorType,
                            message = fullSchedule.message
                        ))
                    }
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

    private fun getScheduleById(scheduleId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (
                    val result = groupScheduleUseCase.getScheduleById(scheduleId)
                ) {
                    is Resource.Success -> {
                        val data = result.data!!
                        when (
                            val fullSchedule = groupScheduleUseCase.getFullSchedule(data)
                        ) {
                            is Resource.Success -> {
                                val scheduleData = fullSchedule.data
                                schedule.postValue(scheduleData)
                                activeSchedule.postValue(scheduleData?.toSavedSchedule())
                                if (scheduleData?.isNotExamsExist() == false) {
                                    examsSchedule.postValue(scheduleData)
                                } else {
                                    examsSchedule.postValue(null)
                                }
                                sharedPrefsUseCase.setActiveScheduleId(fullSchedule.data?.id ?: -1)
                            }
                            is Resource.Error -> {
                                schedule.postValue(Schedule.empty)
                                error.postValue(
                                    StateStatus(
                                        state = StateStatus.ERROR_STATE,
                                        type = fullSchedule.errorType,
                                        message = fullSchedule.message
                                    )
                                )
                            }
                        }
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
        }
    }

    fun deleteSchedule(savedSchedule: SavedSchedule) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (savedSchedule.isGroup) {
                    groupScheduleUseCase.deleteSchedule(savedSchedule)
                } else {
                    employeeScheduleUseCase.deleteSchedule(savedSchedule)
                }
                error.postValue(StateStatus(
                    state = StateStatus.SUCCESS_STATE,
                    type = 0
                ))
                schedule.postValue(Schedule.empty)
                activeSchedule.postValue(null)
                sharedPrefsUseCase.setActiveScheduleId(-1)
            } catch (e: Exception) {
                error.postValue(StateStatus(
                    state = StateStatus.ERROR_STATE,
                    type = Resource.DATA_ERROR, // DELETE_ERROR
                    message = e.message
                ))
            }
        }
    }

}


