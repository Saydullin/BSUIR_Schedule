package com.saydullin.departments.repository

import com.bsuir.domain.model.department.Department
import com.bsuir.domain.repository.department.DepartmentsServerRepository
import com.bsuir.resource.Resource
import com.saydullin.departments.server.service.DepartmentService
import com.saydullin.departments.mapper.DepartmentDataToDomainMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DepartmentsServerRepositoryImpl @Inject constructor(
    private val departmentService: DepartmentService,
    private val departmentDataToDomainMapper: DepartmentDataToDomainMapper,
): DepartmentsServerRepository {

    override suspend fun getAllDepartments(): Resource<List<Department>> {
        return Resource.tryWithSuspend {
            val departments = withContext(Dispatchers.IO) {
                departmentService.getAllDepartments()
            }

            departments.map { departmentDataToDomainMapper.map(it) }
        }
    }

}