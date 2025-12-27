package com.saydullin.departments.repository

import by.devsgroup.database.departments.dao.DepartmentDao
import by.devsgroup.domain.model.departments.Department
import by.devsgroup.domain.repository.department.DepartmentsDatabaseRepository
import by.devsgroup.resource.Resource
import com.saydullin.departments.mapper.DepartmentEntityToDomainMapper
import com.saydullin.departments.mapper.DepartmentToEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DepartmentsDatabaseRepositoryImpl @Inject constructor(
    private val departmentDao: DepartmentDao,
    private val departmentToEntityMapper: DepartmentToEntityMapper,
    private val departmentEntityToDomainMapper: DepartmentEntityToDomainMapper,
): DepartmentsDatabaseRepository {

    override suspend fun getAllDepartments(): Resource<List<Department>> {
        return Resource.tryWithSuspend {
            val departmentEntityList = withContext(Dispatchers.IO) {
                departmentDao.getAllDepartments()
            }

            departmentEntityList.map { departmentEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun getByAbbrev(abbrev: String): Resource<Department?> {
        return Resource.tryWithSuspend {
            val departmentEntity = withContext(Dispatchers.IO) {
                departmentDao.getByAbbrev(abbrev)
            }

            departmentEntityToDomainMapper.map(departmentEntity)
        }
    }

    override suspend fun getListByName(name: String): Resource<List<Department>> {
        return Resource.tryWithSuspend {
            val departmentEntityList = withContext(Dispatchers.IO) {
                departmentDao.getListByName("%$name%")
            }

            departmentEntityList.map { departmentEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun saveDepartments(departments: List<Department>): Resource<Unit> {
        return Resource.tryWithSuspend {
            val departmentsEntity = departments.map { departmentToEntityMapper.map(it) }

            withContext(Dispatchers.IO) { departmentDao.save(departmentsEntity) }
        }
    }

    override suspend fun clear(): Resource<Unit> {
        return Resource.tryWithSuspend {
            withContext(Dispatchers.IO) { departmentDao.clear() }
        }
    }

}