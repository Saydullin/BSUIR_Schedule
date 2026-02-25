package com.bsuir.domain.repository.employees

import com.bsuir.domain.model.employee.Employee
import com.bsuir.resource.Resource

interface EmployeesDatabaseRepository {

    suspend fun getAllEmployees(): Resource<List<Employee>>

    suspend fun getEmployeeById(id: Long): Resource<Employee?>

    suspend fun getEmployeeListByLikeName(name: String): Resource<List<Employee>>

    suspend fun saveEmployeeWithId(employeeId: String, employee: Employee): Resource<Unit>

    suspend fun clear(): Resource<Unit>

}