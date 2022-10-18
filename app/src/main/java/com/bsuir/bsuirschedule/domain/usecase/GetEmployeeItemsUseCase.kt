package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.Employee
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.repository.DepartmentRepository
import com.bsuir.bsuirschedule.domain.repository.EmployeeItemsRepository
import com.bsuir.bsuirschedule.domain.repository.SavedScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class GetEmployeeItemsUseCase(
    private val employeeItemsRepository: EmployeeItemsRepository,
    private val savedScheduleRepository: SavedScheduleRepository,
    private val departmentRepository: DepartmentRepository
) {

    suspend fun getEmployeeItemsAPI(): Resource<ArrayList<Employee>> {
        val result = employeeItemsRepository.getEmployeeItemsAPI()

        return try {
            when(result) {
                is Resource.Success -> {
                    val employees = result.data!!
                    when (
                        val isMergedDepartments = mergeDepartments(employees)
                    ) {
                        is Resource.Success -> {
                            Resource.Success(employees)
                        }
                        is Resource.Error -> {
                            Resource.Error(
                                errorType = isMergedDepartments.errorType,
                                message = isMergedDepartments.message
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    Resource.Error(
                        errorType = result.errorType,
                        message = result.message
                    )
                }
            }
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATA_ERROR,
                message = e.message
            )
        }
    }

    private suspend fun mergeDepartments(employees: ArrayList<Employee>): Resource<Unit> {
        val result = departmentRepository.getDepartments()

        return try {
            when(result) {
                is Resource.Success -> {
                    val data = result.data!!
                    employees.map { employee ->
                        employee.departments = ArrayList()
                        employee.departmentsAbbrList?.map { departmentAbbr ->
                            val department = data.find { it.abbrev.lowercase() == departmentAbbr.lowercase() }
                            if (department != null) {
                                employee.departments.add(department)
                            }
                        } ?: Unit
                    }
                    Resource.Success(null)
                }
                is Resource.Error -> {
                    Resource.Error(
                        errorType = result.errorType,
                        message = result.message
                    )
                }
            }
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATA_ERROR,
                message = e.message
            )
        }
    }

    suspend fun filterByKeywordASC(s: String): Resource<ArrayList<Employee>> {
        return employeeItemsRepository.filterByKeywordASC(s)
    }

    suspend fun filterByKeywordDESC(s: String): Resource<ArrayList<Employee>> {
        return employeeItemsRepository.filterByKeywordDESC(s)
    }

    suspend fun saveEmployeeItem(employeeList: ArrayList<Employee>): Resource<Unit> {
        return employeeItemsRepository.saveEmployeeItem(employeeList)
    }

    suspend fun getEmployeeItems(): Resource<ArrayList<Employee>> {
        return when (
            val result = employeeItemsRepository.getEmployeeItems()
        ) {
            is Resource.Success -> {
                val data = result.data ?: ArrayList()
                val filledIsSaved = fillIsSavedFields(data)
                Resource.Success(filledIsSaved)
            }
            is Resource.Error -> {
                Resource.Error(
                    errorType = result.errorType,
                    message = result.message
                )
            }
        }
    }

    private suspend fun fillIsSavedFields(employeeItems: ArrayList<Employee>): ArrayList<Employee> {
        val savedSchedules = savedScheduleRepository.getSavedSchedules()

        if (savedSchedules is Resource.Success) {
            val data = savedSchedules.data ?: ArrayList()
            employeeItems.map { employee ->
                employee.isSaved = data.find { it.isGroup && it.id == employee.id } != null
            }
        }

        return employeeItems
    }

}


