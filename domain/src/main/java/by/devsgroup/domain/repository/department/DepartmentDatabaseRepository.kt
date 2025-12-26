package by.devsgroup.domain.repository.department

import by.devsgroup.domain.model.departments.Department
import by.devsgroup.resource.Resource

interface DepartmentDatabaseRepository {

    suspend fun getAllDepartments(): Resource<List<Department>>

    suspend fun getByAbbrev(abbrev: String): Resource<Department>

    suspend fun getByName(name: String): Resource<List<Department>>

}