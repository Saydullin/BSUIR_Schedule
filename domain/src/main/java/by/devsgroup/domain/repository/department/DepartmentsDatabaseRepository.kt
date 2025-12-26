package by.devsgroup.domain.repository.department

import by.devsgroup.domain.model.departments.Department
import by.devsgroup.resource.Resource

interface DepartmentsDatabaseRepository {

    suspend fun getAllDepartments(): Resource<List<Department>>

    suspend fun getByAbbrev(abbrev: String): Resource<Department?>

    suspend fun getListByName(name: String): Resource<List<Department>>

    suspend fun saveDepartments(departments: List<Department>): Resource<Unit>

    suspend fun clear(): Resource<Unit>

}