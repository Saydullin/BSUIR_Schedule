package by.devsgroup.employees.usecase

import by.devsgroup.domain.repository.employees.EmployeesDatabaseRepository
import by.devsgroup.domain.repository.employees.EmployeesServerRepository
import by.devsgroup.domain.repository.groups.GroupDatabaseRepository
import by.devsgroup.domain.repository.groups.GroupServerRepository
import by.devsgroup.resource.Resource
import javax.inject.Inject

class GetAndSaveAllEmployeesUseCase @Inject constructor(
    private val employeesDatabaseRepository: EmployeesDatabaseRepository,
    private val employeesServerRepository: EmployeesServerRepository,
) {

    suspend fun execute(): Resource<Unit> {
        return Resource.tryWithSuspend {
            val employees = employeesServerRepository.getAllEmployees()
                .getOrThrow()

            employeesDatabaseRepository.clear()
            employeesDatabaseRepository.saveEmployees(employees)
        }
    }

}