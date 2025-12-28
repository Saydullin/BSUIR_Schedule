package by.devsgroup.domain.repository.specialty

import by.devsgroup.domain.model.specialty.Specialty
import by.devsgroup.resource.Resource

interface SpecialtyDatabaseRepository {

    suspend fun getById(id: Long): Resource<Specialty?>

    suspend fun saveList(specialties: List<Specialty>): Resource<Unit>

    suspend fun clear(): Resource<Unit>

}