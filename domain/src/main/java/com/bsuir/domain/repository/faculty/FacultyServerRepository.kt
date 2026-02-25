package com.bsuir.domain.repository.faculty

import com.bsuir.domain.model.faculty.Faculty
import com.bsuir.resource.Resource

interface FacultyServerRepository {

    suspend fun getAllFaculties(): Resource<List<Faculty>>

}