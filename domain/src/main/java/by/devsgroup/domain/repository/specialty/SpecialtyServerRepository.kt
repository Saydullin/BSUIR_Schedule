package by.devsgroup.domain.repository.specialty

import by.devsgroup.domain.model.specialty.Specialty
import by.devsgroup.resource.Resource

interface SpecialtyServerRepository {

    suspend fun getAllSpecialties(): Resource<List<Specialty>>

}