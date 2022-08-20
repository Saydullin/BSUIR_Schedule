package com.example.bsuirschedule.domain.usecase

import com.example.bsuirschedule.domain.models.Employee
import com.example.bsuirschedule.domain.repository.DepartmentRepository
import com.example.bsuirschedule.domain.repository.EmployeeItemsRepository
import com.example.bsuirschedule.domain.utils.Resource

class GetEmployeeItemsUseCase(
    private val employeeItemsRepository: EmployeeItemsRepository,
    private val departmentRepository: DepartmentRepository
) {

    suspend fun getEmployeeItemsAPI(): Resource<ArrayList<Employee>> {
        val result = employeeItemsRepository.getEmployeeItemsAPI()

        return try {
            when(result) {
                is Resource.Success -> {
                    val employees = result.data!!
                    val isMergedDepartments = mergeDepartments(employees)
                    when(isMergedDepartments) {
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

    suspend fun getEmployeeItems(): Resource<ArrayList<Employee>> {
        return employeeItemsRepository.getEmployeeItems()
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

    suspend fun deleteEmployeeItem(employee: Employee): Resource<Unit> {
        return employeeItemsRepository.deleteEmployeeItem(employee)
    }

    suspend fun getEmployeesByName(employeeName: String): Resource<ArrayList<Employee>> {
       return  employeeItemsRepository.getEmployeeItemByName(employeeName)
    }

}


