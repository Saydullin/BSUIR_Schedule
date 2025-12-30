package com.saydullin.departments.repository

import by.devsgroup.database.departments.dao.EmployeeDepartmentDao
import by.devsgroup.database.departments.entity.EmployeeDepartmentEntity
import by.devsgroup.domain.model.department.Department
import by.devsgroup.domain.repository.department.EmployeeDepartmentsDatabaseRepository
import by.devsgroup.resource.Resource
import com.saydullin.departments.mapper.EmployeeDepartmentEntityToDomainMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeeDepartmentsDatabaseRepositoryImpl @Inject constructor(
    private val employeeDepartmentDao: EmployeeDepartmentDao,
    private val employeeDepartmentEntityToDomainMapper: EmployeeDepartmentEntityToDomainMapper,
): EmployeeDepartmentsDatabaseRepository {

    override suspend fun getByEmployeeId(employeeId: String): Resource<Department?> {
        return Resource.tryWithSuspend {
            val departmentEntity = withContext(Dispatchers.IO) {
                employeeDepartmentDao.getByEmployeeId(employeeId)
            }

            employeeDepartmentEntityToDomainMapper.map(departmentEntity)
        }
    }

    override suspend fun save(
        employeeId: String,
        department: Department
    ): Resource<Unit> {
        return Resource.tryWithSuspend {
            val departmentEntity = EmployeeDepartmentEntity(
                id = department.id,
                name = department.name,
                abbrev = department.abbrev,
                urlId = department.urlId,
                employeeId = employeeId,
            )

            withContext(Dispatchers.IO) {
                employeeDepartmentDao.save(departmentEntity)
            }
        }
    }

    override suspend fun clear(): Resource<Unit> {
        return Resource.tryWithSuspend {
            withContext(Dispatchers.IO) { employeeDepartmentDao.clear() }
        }
    }

}