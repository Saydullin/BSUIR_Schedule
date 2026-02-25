package com.bsuir.employees.usecase

import com.bsuir.domain.repository.department.DepartmentsDatabaseRepository
import com.bsuir.domain.repository.department.EmployeeDepartmentsDatabaseRepository
import com.bsuir.domain.repository.employees.EmployeesDatabaseRepository
import com.bsuir.domain.repository.employees.EmployeesServerRepository
import com.bsuir.resource.Resource
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

                employeesDatabaseRepository.saveEmployeeWithId(employeeId, employee)

                employee.academicDepartment?.map { departmentAbbrev ->
                    val department = departmentsDatabaseRepository.getByAbbrev(departmentAbbrev)
                        .getOrNull() ?: return@map

                    employeeDepartmentsDatabaseRepository.save(employeeId, department)
                }
            }
        }
    }

}


