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

    private val isFacultiesDone = MutableLiveData(false)
    private val isSpecialitiesDone = MutableLiveData(false)
    private val isDepartmentsDone = MutableLiveData(false)
    private val isTrouble = MutableLiveData(false)
    private val allDoneMerger: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    val isTroubleStatus = isTrouble
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
            when (
                val result = getFacultyUseCase.getFacultiesAPI()
            ) {
                is Resource.Success -> {
                    when (
                        getFacultyUseCase.saveFaculties(result.data!!)
                    ) {
                        is Resource.Success -> {
                            isTrouble.postValue(false)
                        }
                        is Resource.Error -> {
                            isTrouble.postValue(true)
                        }
                    }
                }
                is Resource.Error -> {
                    isTrouble.postValue(true)
                }
            }
            isFacultiesDone.postValue(true)
        }
    }

    private fun updateSpecialities() {
        isSpecialitiesDone.value = false
        viewModelScope.launch(IO) {
            when (
                val result = getSpecialityUseCase.getSpecialitiesAPI()
            ) {
                is Resource.Success -> {
                    when (
                        getSpecialityUseCase.saveSpecialities(result.data!!)
                    ) {
                        is Resource.Success -> {
                            isTrouble.postValue(false)
                        }
                        is Resource.Error -> {
                            isTrouble.postValue(true)
                        }
                    }
                }
                is Resource.Error -> {
                    isTrouble.postValue(true)
                }
            }
            isSpecialitiesDone.postValue(true)
        }
    }

    private fun updateDepartments() {
        isDepartmentsDone.value = false
        viewModelScope.launch(IO) {
            when (
                val result = getDepartmentUseCase.getDepartmentsAPI()
            ) {
                is Resource.Success -> {
                    when (
                        getDepartmentUseCase.saveDepartments(result.data!!)
                    ) {
                        is Resource.Success -> {
                            isTrouble.postValue(false)
                        }
                        is Resource.Error -> {
                            isTrouble.postValue(true)
                        }
                    }
                }
                is Resource.Error -> {
                    isTrouble.postValue(true)
                }
            }
            isDepartmentsDone.postValue(true)
        }
    }

}


