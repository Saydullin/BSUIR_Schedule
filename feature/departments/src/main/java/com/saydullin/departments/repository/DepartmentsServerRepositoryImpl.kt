package com.saydullin.departments.repository

import by.devsgroup.domain.model.departments.Department
import by.devsgroup.domain.repository.department.DepartmentsServerRepository
import by.devsgroup.resource.Resource
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