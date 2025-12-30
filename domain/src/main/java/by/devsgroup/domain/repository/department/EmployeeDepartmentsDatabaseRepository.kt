package by.devsgroup.domain.repository.department

import by.devsgroup.domain.model.department.Department
import by.devsgroup.resource.Resource

interface EmployeeDepartmentsDatabaseRepository {

    suspend fun getByEmployeeId(employeeId: String): Resource<Department?>

    suspend fun save(employeeId: String, department: Department): Resource<Unit>

    suspend fun clear(): Resource<Unit>

}