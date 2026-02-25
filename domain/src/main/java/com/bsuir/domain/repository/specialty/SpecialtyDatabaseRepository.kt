package com.bsuir.domain.repository.specialty

import com.bsuir.domain.model.specialty.Specialty
import com.bsuir.resource.Resource

interface SpecialtyDatabaseRepository {

    suspend fun getById(id: Long): Resource<Specialty?>

    suspend fun saveList(specialties: List<Specialty>): Resource<Unit>

    suspend fun clear(): Resource<Unit>

}