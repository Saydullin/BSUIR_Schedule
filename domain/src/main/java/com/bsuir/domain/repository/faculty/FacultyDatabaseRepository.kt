package com.bsuir.domain.repository.faculty

import com.bsuir.domain.model.faculty.Faculty
import com.bsuir.resource.Resource

interface FacultyDatabaseRepository {

    suspend fun getById(id: Long): Resource<Faculty?>

    suspend fun saveList(faculties: List<Faculty>): Resource<Unit>

    suspend fun clear(): Resource<Unit>

}