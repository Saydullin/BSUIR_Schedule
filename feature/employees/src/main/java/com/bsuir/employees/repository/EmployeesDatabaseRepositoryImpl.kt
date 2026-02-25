package com.bsuir.employees.repository

import com.bsuir.database.employees.dao.EmployeeDao
import com.bsuir.domain.model.employee.Employee
import com.bsuir.domain.repository.employees.EmployeesDatabaseRepository
import com.bsuir.employees.mapper.EmployeeEntityToDomainMapper
import com.bsuir.employees.mapper.EmployeeToEntityMapper
import com.bsuir.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeesDatabaseRepositoryImpl @Inject constructor(
    private val employeeDao: EmployeeDao,
    private val employeeEntityToDomainMapper: EmployeeEntityToDomainMapper,
    private val employeeToEntityMapper: EmployeeToEntityMapper,
): EmployeesDatabaseRepository {

    override suspend fun getAllEmployees(): Resource<List<Employee>> {
        return Resource.tryWithSuspend {
            val employeeEntityList = withContext(Dispatchers.IO) { employeeDao.getAllEmployees() }

            employeeEntityList.map { employeeEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun getEmployeeById(id: Long): Resource<Employee?> {
        return Resource.tryWithSuspend {
            val employeeEntity = withContext(Dispatchers.IO) { employeeDao.getById(id) }

            employeeEntity?.let { employeeEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun getEmployeeListByLikeName(name: String): Resource<List<Employee>> {
        return Resource.tryWithSuspend {
            val employeeEntityList = withContext(Dispatchers.IO) {
                employeeDao.getListByName("%$name%")
            }

            employeeEntityList.map { employeeEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun saveEmployeeWithId(employeeId: String, employee: Employee): Resource<Unit> {
        return Resource.tryWithSuspend {
            val employeesEntity = employeeToEntityMapper.map(employee).copy(departmentKeyId = employeeId)

            withContext(Dispatchers.IO) { employeeDao.save(employeesEntity) }
        }
    }

    override suspend fun clear(): Resource<Unit> {
        return Resource.tryWithSuspend {
            withContext(Dispatchers.IO) { employeeDao.clear() }
        }
    }

}