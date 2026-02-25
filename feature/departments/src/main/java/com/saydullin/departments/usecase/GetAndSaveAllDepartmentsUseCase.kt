package com.saydullin.departments.usecase

import com.bsuir.domain.repository.department.DepartmentsDatabaseRepository
import com.bsuir.domain.repository.department.DepartmentsServerRepository
import com.bsuir.resource.Resource
import javax.inject.Inject

class GetAndSaveAllDepartmentsUseCase @Inject constructor(
    private val departmentsServerRepository: DepartmentsServerRepository,
    private val departmentsDatabaseRepository: DepartmentsDatabaseRepository,
) {

    suspend fun execute(): Resource<Unit> {
        return Resource.tryWithSuspend {
            val departments = departmentsServerRepository.getAllDepartments()
                .getOrThrow()

            departmentsDatabaseRepository.clear()
            departmentsDatabaseRepository.saveDepartments(departments)
        }
    }

}