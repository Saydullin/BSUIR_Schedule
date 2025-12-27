package by.devsgroup.employees.repository

import by.devsgroup.database.employees.dao.EmployeeDao
import by.devsgroup.domain.model.employees.Employee
import by.devsgroup.domain.repository.employees.EmployeesDatabaseRepository
import by.devsgroup.employees.mapper.EmployeeEntityToDomainMapper
import by.devsgroup.employees.mapper.EmployeeToEntityMapper
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
            val employeeEntityList = withContext(Dispatchers.IO) { employeeDao.getAllEmployees() }

            employeeEntityList.map { employeeEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun getEmployeeById(id: Int): Resource<Employee?> {
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