package by.devsgroup.domain.repository.employees

import by.devsgroup.domain.model.employees.Employee
import by.devsgroup.resource.Resource

interface EmployeesServerRepository {

    suspend fun getAllEmployees(): Resource<List<Employee>>

}


