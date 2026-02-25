package com.bsuir.domain.repository.department

import com.bsuir.domain.model.department.Department
import com.bsuir.resource.Resource

interface DepartmentsServerRepository {

    suspend fun getAllDepartments(): Resource<List<Department>>

}