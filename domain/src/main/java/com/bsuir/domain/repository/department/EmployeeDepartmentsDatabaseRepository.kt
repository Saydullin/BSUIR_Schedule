package com.bsuir.domain.repository.department

import com.bsuir.domain.model.department.Department
import com.bsuir.resource.Resource

interface EmployeeDepartmentsDatabaseRepository {

    suspend fun getByEmployeeId(employeeId: String): Resource<Department?>

    suspend fun save(employeeId: String, department: Department): Resource<Unit>

    suspend fun clear(): Resource<Unit>

}