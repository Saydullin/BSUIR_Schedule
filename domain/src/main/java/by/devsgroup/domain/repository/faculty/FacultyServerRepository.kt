package by.devsgroup.domain.repository.faculty

import by.devsgroup.domain.model.faculty.Faculty
import by.devsgroup.resource.Resource

interface FacultyServerRepository {

    suspend fun getAllFaculties(): Resource<List<Faculty>>

}