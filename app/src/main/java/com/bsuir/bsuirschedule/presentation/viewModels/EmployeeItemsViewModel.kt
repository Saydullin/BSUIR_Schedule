package com.bsuir.bsuirschedule.presentation.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsuir.bsuirschedule.domain.models.Employee
import com.bsuir.bsuirschedule.domain.models.StateStatus
import com.bsuir.bsuirschedule.domain.usecase.GetDepartmentUseCase
import com.bsuir.bsuirschedule.domain.usecase.GetEmployeeItemsUseCase
import com.bsuir.bsuirschedule.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmployeeItemsViewModel(
    private val getEmployeeItemsUseCase: GetEmployeeItemsUseCase,
    private val getDepartmentUseCase: GetDepartmentUseCase
): ViewModel() {

    private val employeeItems = MutableLiveData<ArrayList<Employee>>(null)
    private val error = MutableLiveData<StateStatus>(null)
    private val loading = MutableLiveData(false)
    private val isUpdating = MutableLiveData(false)
    val isUpdatingStatus = isUpdating
    val errorStatus = error
    val employeeItemsStatus = employeeItems

    fun closeError() {
        error.value = null
    }

    private suspend fun getDepartments() {
        when (
            val departments = getDepartmentUseCase.getDepartmentsAPI()
        ) {
            is Resource.Success -> {
                getDepartmentUseCase.saveDepartments(departments.data!!)
            }
            is Resource.Error -> {
//                error.postValue(
//                    StateStatus(
//                        state = StateStatus.ERROR_STATE,
//                        type = departments.errorType,
//                        message = departments.message
//                    )
//                )
//                FIXME Send Error

            }
        }
    }

    fun updateDepartmentsAndEmployeeItems() {
        viewModelScope.launch(Dispatchers.IO) {
            isUpdating.postValue(true)
            loading.postValue(true)
            val departments = launch(Dispatchers.IO) {
                getDepartments()
            }
            departments.join()
            updateEmployeeItems()
            loading.postValue(false)
            isUpdating.postValue(false)
        }
    }

    // Update all employee items with API
    private suspend fun updateEmployeeItems() {
        when (
            val newEmployeeItems = getEmployeeItemsUseCase.getEmployeeItemsAPI()
        ) {
            is Resource.Success -> {
                when (
                    val responseSave = getEmployeeItemsUseCase.saveEmployeeItem(newEmployeeItems.data!!)
                ) {
                    is Resource.Success -> {
                        getAllEmployeeItems()
                    }
                    is Resource.Error -> {
                        error.postValue(
                            StateStatus(
                                state = StateStatus.ERROR_STATE,
                                type = responseSave.errorType,
                                message = responseSave.message
                            )
                        )
                    }
                }
            }
            is Resource.Error -> {
                error.postValue(
                    StateStatus(
                        state = StateStatus.ERROR_STATE,
                        type = newEmployeeItems.errorType,
                        message = newEmployeeItems.message
                    )
                )
            }
        }
    }

    // All Employee Items
    fun getAllEmployeeItems() {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            when (
                val result = getEmployeeItemsUseCase.getEmployeeItems()
            ) {
                is Resource.Success -> {
                    employeeItems.postValue(result.data!!)
                }
                is Resource.Error -> {
                    error.postValue(
                        StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = result.errorType,
                            message = result.message
                        )
                    )
                }
            }
            loading.postValue(false)
        }
    }

    fun saveEmployeeItem(employee: Employee) {
        viewModelScope.launch(Dispatchers.IO) {
            getEmployeeItemsUseCase.saveEmployeeItem(arrayListOf(employee))
        }
    }

    fun filterByKeyword(s: String, isAsc: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = if (isAsc) {
                getEmployeeItemsUseCase.filterByKeywordASC(s)
            } else {
                getEmployeeItemsUseCase.filterByKeywordDESC(s)
            }
            when(result) {
                is Resource.Success -> {
                    employeeItems.postValue(result.data)
                }
                is Resource.Error -> {
                    error.postValue(
                        StateStatus(
                            state = StateStatus.ERROR_STATE,
                            type = result.errorType,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

}


