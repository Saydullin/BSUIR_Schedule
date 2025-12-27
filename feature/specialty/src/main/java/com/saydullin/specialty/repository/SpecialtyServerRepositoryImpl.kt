package com.saydullin.specialty.repository

import by.devsgroup.domain.model.specialty.Specialty
import by.devsgroup.domain.repository.specialty.SpecialtyServerRepository
import by.devsgroup.resource.Resource
import com.saydullin.specialty.mapper.SpecialtyDataToDomainMapper
import com.saydullin.specialty.server.service.SpecialtyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpecialtyServerRepositoryImpl @Inject constructor(
    private val specialtyService: SpecialtyService,
    private val specialtyDataToDomainMapper: SpecialtyDataToDomainMapper,
): SpecialtyServerRepository {

    override suspend fun getAllSpecialties(): Resource<List<Specialty>> {
        return Resource.tryWithSuspend {
            val specialties = withContext(Dispatchers.IO) {
                specialtyService.getAllSpecialities()
            }

            specialties.map { specialtyDataToDomainMapper.map(it) }
        }
    }

}