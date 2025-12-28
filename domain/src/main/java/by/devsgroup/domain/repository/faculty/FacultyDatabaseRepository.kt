package by.devsgroup.domain.repository.faculty

import by.devsgroup.domain.model.faculty.Faculty
import by.devsgroup.resource.Resource

interface FacultyDatabaseRepository {

    suspend fun getById(id: Long): Resource<Faculty?>

    suspend fun saveList(faculties: List<Faculty>): Resource<Unit>

    suspend fun clear(): Resource<Unit>

}