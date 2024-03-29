package com.bsuir.bsuirschedule.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsuir.bsuirschedule.domain.models.CurrentWeek
import com.bsuir.bsuirschedule.domain.models.StateStatus
import com.bsuir.bsuirschedule.domain.usecase.GetCurrentWeekUseCase
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.StatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentWeekViewModel(
    private val getCurrentWeekUseCase: GetCurrentWeekUseCase
): ViewModel() {

    private val currentWeek = MutableLiveData(0)
    private val error = MutableLiveData<StateStatus>(null)
    private val loading = MutableLiveData(false)
    val currentWeekStatus = currentWeek
    val errorStatus = error

    fun closeError() {
        error.value = null
    }

    fun setCurrentWeekNumber() {
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val result = getCurrentWeekUseCase.isCurrentWeekPassed()
            ) {
                is Resource.Success -> {
                    if (result.data == true) {
                        getCurrentWeekAPI()
                    }
                }
                is Resource.Error -> {
                    error.postValue(
                        StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = result.statusCode,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    fun getCurrentWeekAPI() {
        viewModelScope.launch(Dispatchers.IO) {
            when(
                val result = getCurrentWeekUseCase.getCurrentWeekAPI()
            ) {
                is Resource.Success -> {
                    val data = result.data!!
                    saveCurrentWeek(data)
                    currentWeek.postValue(data.week)
                }
                is Resource.Error -> {
                    error.postValue(
                        StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = StatusCode.WEEK_API_LOADING_ERROR,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    private fun saveCurrentWeek(currentWeekNumber: CurrentWeek) {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            when(
                val result = getCurrentWeekUseCase.saveCurrentWeek(currentWeekNumber)
            ) {
                is Resource.Success -> {
                    loading.postValue(false)
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

    fun getCurrentWeek() {
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val result = getCurrentWeekUseCase.getCurrentWeek()
            ) {
                is Resource.Success -> {
                    currentWeek.postValue(result.data!!)
                }
                is Resource.Error -> {
                    currentWeek.postValue(0)
                }
            }
        }
    }

}


