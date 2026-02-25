package com.bsuir.domain.repository.specialty

import com.bsuir.domain.model.specialty.Specialty
import com.bsuir.resource.Resource

interface SpecialtyServerRepository {

    suspend fun getAllSpecialties(): Resource<List<Specialty>>

}