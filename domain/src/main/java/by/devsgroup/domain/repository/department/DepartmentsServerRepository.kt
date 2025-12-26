package by.devsgroup.domain.repository.department

import by.devsgroup.domain.model.departments.Department
import by.devsgroup.resource.Resource

interface DepartmentsServerRepository {

    suspend fun getAllDepartments(): Resource<List<Department>>

}