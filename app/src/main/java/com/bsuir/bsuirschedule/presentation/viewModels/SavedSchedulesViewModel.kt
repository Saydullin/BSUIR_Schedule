package com.bsuir.bsuirschedule.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsuir.bsuirschedule.domain.models.StateStatus
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.usecase.GetSavedScheduleUseCase
import com.bsuir.bsuirschedule.domain.usecase.SharedPrefsUseCase
import com.bsuir.bsuirschedule.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedSchedulesViewModel(
    private val getSavedScheduleUseCase: GetSavedScheduleUseCase,
    private val sharedPrefsUseCase: SharedPrefsUseCase
): ViewModel() {

    private val loading = MutableLiveData(false)
    private val updatedSchedule = MutableLiveData<SavedSchedule>(null)
    private val activeSchedule = MutableLiveData<SavedSchedule>()
    private val error = MutableLiveData<StateStatus>(null)
    private val savedSchedules = MutableLiveData<ArrayList<SavedSchedule>>(null)
    private val savedSchedulesCount = MutableLiveData<Int>(null)
    val updatedScheduleStatus = updatedSchedule
    val activeScheduleStatusCount = savedSchedulesCount
    val savedSchedulesStatus = savedSchedules
    val errorMessageStatus = error

    fun closeError() {
        error.value = null
    }

    fun updateSavedSchedulesCount() {
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val result = getSavedScheduleUseCase.getSavedSchedules()
            ) {
                is Resource.Success -> {
                    savedSchedulesCount.postValue(result.data!!.size)
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

    fun getSavedSchedules() {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            when (
                val result = getSavedScheduleUseCase.getSavedSchedules()
            ) {
                is Resource.Success -> {
                    if (result.data!!.size == 0) {
                        savedSchedules.postValue(null)
                        savedSchedulesCount.postValue(null)
                    } else {
                        savedSchedules.postValue(result.data)
                        savedSchedulesCount.postValue(result.data.size)
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.statusCode,
                        message = result.message
                    ))
                }
            }
            loading.postValue(false)
        }
    }

    fun filterByKeyword(title: String, isAsc: Boolean = true) {
        loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = if (isAsc) {
                getSavedScheduleUseCase.filterByKeywordASC(title)
            } else {
                getSavedScheduleUseCase.filterByKeywordDESC(title)
            }
            when(result) {
                is Resource.Success -> {
                    savedSchedulesCount.postValue(result.data!!.size)
                    savedSchedules.postValue(result.data)
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.statusCode,
                        message = result.message
                    ))
                }
            }
            loading.postValue(false)
        }
    }

    fun setActiveSchedule(schedule: SavedSchedule) {
        activeSchedule.value = schedule
        sharedPrefsUseCase.setActiveScheduleId(schedule.id)
    }

    fun updateSchedule(schedule: SavedSchedule) {
        viewModelScope.launch(Dispatchers.IO) {
            updatedSchedule.postValue(null)
            when (
                val result = getSavedScheduleUseCase.saveSchedule(schedule)
            ) {
                is Resource.Success -> {
                    updatedSchedule.postValue(schedule)
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

    fun saveSchedule(schedule: SavedSchedule) {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            when (
                val result = getSavedScheduleUseCase.saveSchedule(schedule)
            ) {
                is Resource.Success -> {
                    activeSchedule.postValue(schedule)
                    if (schedule.isGroup) {
                        sharedPrefsUseCase.setActiveScheduleId(schedule.group.id)
                    } else {
                        sharedPrefsUseCase.setActiveScheduleId(schedule.employee.id)
                    }
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
        loading.value = false
    }

    fun deleteSchedule(schedule: SavedSchedule) {
        loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = getSavedScheduleUseCase.deleteSchedule(schedule)
            if (result is Resource.Error) {
                error.postValue(StateStatus(
                    state = StateStatus.ERROR_STATE,
                    type = result.statusCode,
                    message = result.message
                ))
            }
        }
        loading.value = false
    }

}


