package com.bsuir.domain.repository.employees

import com.bsuir.domain.model.employee.Employee
import com.bsuir.resource.Resource

interface EmployeesServerRepository {

    suspend fun getAllEmployees(): Resource<List<Employee>>

}


