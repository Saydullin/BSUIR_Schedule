package com.example.bsuirschedule.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bsuirschedule.domain.models.CurrentWeek
import com.example.bsuirschedule.domain.models.StateStatus
import com.example.bsuirschedule.domain.usecase.GetCurrentWeekUseCase
import com.example.bsuirschedule.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentWeekViewModel(
    private val getCurrentWeekUseCase: GetCurrentWeekUseCase
): ViewModel() {

    private val currentWeek = MutableLiveData(0)
    private val error = MutableLiveData<StateStatus>()
    private val loading = MutableLiveData(false)
    val currentWeekStatus = currentWeek
    val errorStatus = error

    fun closeError() {
        error.value = null
    }

    fun getCurrentWeekAPI() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getCurrentWeekUseCase.getCurrentWeekAPI()
            when(result) {
                is Resource.Success -> {
                    val data = result.data!!
                    saveCurrentWeek(data)
                    currentWeek.postValue(data.week)
                }
                is Resource.Error -> {
                    error.postValue(StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = result.errorType,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    private fun saveCurrentWeek(currentWeek: CurrentWeek) {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            val result = getCurrentWeekUseCase.saveCurrentWeek(currentWeek)
            when(result) {
                is Resource.Success -> {
                    loading.postValue(false)
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

    fun getCurrentWeek() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getCurrentWeekUseCase.getCurrentWeek()
            when(result) {
                is Resource.Success -> {
                    val data = result.data!!
                    currentWeek.postValue(data.week)
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

}