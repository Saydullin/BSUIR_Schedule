package by.devsgroup.employees.usecase

import by.devsgroup.domain.repository.department.DepartmentsDatabaseRepository
import by.devsgroup.domain.repository.department.EmployeeDepartmentsDatabaseRepository
import by.devsgroup.domain.repository.employees.EmployeesDatabaseRepository
import by.devsgroup.domain.repository.employees.EmployeesServerRepository
import by.devsgroup.resource.Resource
import java.util.UUID
import javax.inject.Inject

class GetAndSaveAllEmployeesUseCase @Inject constructor(
    private val employeeDepartmentsDatabaseRepository: EmployeeDepartmentsDatabaseRepository,
    private val departmentsDatabaseRepository: DepartmentsDatabaseRepository,
    private val employeesDatabaseRepository: EmployeesDatabaseRepository,
    private val employeesServerRepository: EmployeesServerRepository,
) {

    suspend fun execute(): Resource<Unit> {
        return Resource.tryWithSuspend {
            val employees = employeesServerRepository.getAllEmployees()
                .getOrThrow()

            employeesDatabaseRepository.clear()

            employees.map { employee ->
                val employeeId = UUID.randomUUID().toString()

                employee.academicDepartment?.map { departmentAbbrev ->
                    val department = departmentsDatabaseRepository.getByAbbrev(departmentAbbrev)
                        .getOrNull() ?: return@map

                    employeeDepartmentsDatabaseRepository.save(employeeId, department)
                }

                employeesDatabaseRepository.saveEmployeeWithId(employeeId, employee)
            }
        }
    }

}


