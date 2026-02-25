package com.bsuir.employees.repository

import com.bsuir.domain.model.employee.Employee
import com.bsuir.domain.repository.employees.EmployeesServerRepository
import com.bsuir.employees.mapper.EmployeeDataToDomainMapper
import com.bsuir.employees.server.service.EmployeesService
import com.bsuir.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeesServerRepositoryImpl @Inject constructor(
    private val employeesService: EmployeesService,
    private val employeeDataToDomainMapper: EmployeeDataToDomainMapper,
): EmployeesServerRepository {

    override suspend fun getAllEmployees(): Resource<List<Employee>> {
        return Resource.tryWithSuspend {
            val employees = withContext(Dispatchers.IO) { employeesService.getAllEmployees() }

            employees.map { employeeDataToDomainMapper.map(it) }
        }
    }

}