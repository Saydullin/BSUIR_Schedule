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
    private val activeSchedule = MutableLiveData<SavedSchedule>()
    private val error = MutableLiveData<StateStatus>(null)
    private val savedSchedules = MutableLiveData<ArrayList<SavedSchedule>>(null)
    val activeScheduleStatus = activeSchedule
    val savedSchedulesStatus = savedSchedules
    val errorMessageStatus = error

    fun closeError() {
        error.value = null
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
                    } else {
                        savedSchedules.postValue(result.data)
                    }
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.errorType,
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
                    savedSchedules.postValue(result.data)
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = result.errorType,
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
                        type = result.errorType,
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
            when (
                val result = getSavedScheduleUseCase.deleteSchedule(schedule)
            ) {
                is Resource.Success -> {
                    getSavedSchedules()
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
        loading.value = false
    }

}


