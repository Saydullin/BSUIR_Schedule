package by.devsgroup.employees.repository

import by.devsgroup.domain.model.employees.Employee
import by.devsgroup.domain.repository.employees.EmployeesDatabaseRepository
import by.devsgroup.employees.data.db.dao.EmployeeDao
import by.devsgroup.employees.data.mapper.EmployeeEntityToDomainMapper
import by.devsgroup.employees.data.mapper.EmployeeToEntityMapper
import by.devsgroup.resource.Resource
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
            val groupEntityList = withContext(Dispatchers.IO) { employeeDao.getAllEmployees() }

            groupEntityList.map { employeeEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun getEmployeeById(id: Int): Resource<Employee?> {
        return Resource.tryWithSuspend {
            val groupEntity = withContext(Dispatchers.IO) { employeeDao.getById(id) }

            groupEntity?.let { employeeEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun getEmployeeListByLikeName(name: String): Resource<List<Employee>> {
        return Resource.tryWithSuspend {
            val groupEntityList = withContext(Dispatchers.IO) { employeeDao.getListByName("%$name%") }

            groupEntityList.map { employeeEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun saveEmployees(employees: List<Employee>): Resource<Unit> {
        return Resource.tryWithSuspend {
            val employeesEntity = employees.map { employeeToEntityMapper.map(it) }

            withContext(Dispatchers.IO) { employeeDao.save(employeesEntity) }
        }
    }

    override suspend fun clear(): Resource<Unit> {
        return Resource.tryWithSuspend {
            withContext(Dispatchers.IO) { employeeDao.clear() }
        }
    }

}