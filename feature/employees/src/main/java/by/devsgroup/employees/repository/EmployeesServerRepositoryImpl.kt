package by.devsgroup.employees.repository

import by.devsgroup.domain.model.employees.Employee
import by.devsgroup.domain.repository.employees.EmployeesServerRepository
import by.devsgroup.employees.mapper.EmployeeDataToDomainMapper
import by.devsgroup.employees.data.server.service.EmployeesService
import by.devsgroup.resource.Resource
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