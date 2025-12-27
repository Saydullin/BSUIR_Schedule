package by.devsgroup.domain.repository.employees

import by.devsgroup.domain.model.employees.Employee
import by.devsgroup.resource.Resource

interface EmployeesDatabaseRepository {

    suspend fun getAllEmployees(): Resource<List<Employee>>

    suspend fun getEmployeeById(id: Int): Resource<Employee?>

    suspend fun getEmployeeListByLikeName(name: String): Resource<List<Employee>>

    suspend fun saveEmployeeWithId(employeeId: String, employee: Employee): Resource<Unit>

    suspend fun clear(): Resource<Unit>

}