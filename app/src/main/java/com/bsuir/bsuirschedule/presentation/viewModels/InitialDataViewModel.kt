package com.bsuir.bsuirschedule.presentation.viewModels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsuir.bsuirschedule.domain.models.StateStatus
import com.bsuir.bsuirschedule.domain.usecase.GetDepartmentUseCase
import com.bsuir.bsuirschedule.domain.usecase.GetFacultyUseCase
import com.bsuir.bsuirschedule.domain.usecase.GetSpecialityUseCase
import com.bsuir.bsuirschedule.domain.utils.Resource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class InitialDataViewModel(
    private val getFacultyUseCase: GetFacultyUseCase,
    private val getSpecialityUseCase: GetSpecialityUseCase,
    private val getDepartmentUseCase: GetDepartmentUseCase
): ViewModel() {

    private val isFacultiesDone: MutableLiveData<Boolean> = MutableLiveData(false)
    private val isSpecialitiesDone: MutableLiveData<Boolean> = MutableLiveData(false)
    private val isDepartmentsDone: MutableLiveData<Boolean> = MutableLiveData(false)
    private val allDoneMerger: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    private val error = MutableLiveData<StateStatus>(null)
    val errorStatus = error
    val allDoneStatus = allDoneMerger

    fun initAllData() {
        allDoneMerger.addSource(isFacultiesDone) { value ->
            allDoneMerger.value = value &&
                    isSpecialitiesDone.value == true &&
                    isDepartmentsDone.value == true
        }
        allDoneMerger.addSource(isDepartmentsDone) { value ->
            allDoneMerger.value = value &&
                    isSpecialitiesDone.value == true &&
                    isFacultiesDone.value == true
        }
        allDoneMerger.addSource(isSpecialitiesDone) { value ->
            allDoneMerger.value = value &&
                    isDepartmentsDone.value == true &&
                    isFacultiesDone.value == true
        }
        updateFaculties()
        updateSpecialities()
        updateDepartments()
    }

    private fun updateFaculties() {
        isFacultiesDone.value = false
        viewModelScope.launch(IO) {
            val result = getFacultyUseCase.getFacultiesAPI()
            when(result) {
                is Resource.Success -> {
                    val savedResult = getFacultyUseCase.saveFaculties(result.data!!)
                    if (savedResult is Resource.Error) {
                        error.postValue(StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = savedResult.errorType,
                            message = savedResult.message
                        ))
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
            isFacultiesDone.postValue(true)
        }
    }

    private fun updateSpecialities() {
        isSpecialitiesDone.value = false
        viewModelScope.launch(IO) {
            val result = getSpecialityUseCase.getSpecialitiesAPI()
            when(result) {
                is Resource.Success -> {
                    val savedResult = getSpecialityUseCase.saveSpecialities(result.data!!)
                    if (savedResult is Resource.Error) {
                        error.postValue(StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = result.errorType,
                            message = result.message
                        ))
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
            isSpecialitiesDone.postValue(true)
        }
    }

    private fun updateDepartments() {
        isDepartmentsDone.value = false
        viewModelScope.launch(IO) {
            val result = getDepartmentUseCase.getDepartmentsAPI()
            when(result) {
                is Resource.Success -> {
                    val savedResult = getDepartmentUseCase.saveDepartments(result.data!!)
                    if (savedResult is Resource.Error) {
                        error.postValue(StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = result.errorType,
                            message = result.message
                        ))
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
            isDepartmentsDone.postValue(true)
        }
    }

}


