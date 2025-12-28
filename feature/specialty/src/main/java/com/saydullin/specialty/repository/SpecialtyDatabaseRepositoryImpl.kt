package com.saydullin.specialty.repository

import by.devsgroup.database.specialty.dao.SpecialtyDao
import by.devsgroup.domain.model.specialty.Specialty
import by.devsgroup.domain.repository.specialty.SpecialtyDatabaseRepository
import by.devsgroup.resource.Resource
import com.saydullin.specialty.mapper.SpecialtyEntityToDomainMapper
import com.saydullin.specialty.mapper.SpecialtyToEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpecialtyDatabaseRepositoryImpl @Inject constructor(
    private val specialtyDao: SpecialtyDao,
    private val specialtyToEntityMapper: SpecialtyToEntityMapper,
    private val specialtyEntityToDomainMapper: SpecialtyEntityToDomainMapper,
): SpecialtyDatabaseRepository {

    override suspend fun getById(id: Long): Resource<Specialty?> {
        return Resource.tryWithSuspend {
            val facultyEntity = withContext(Dispatchers.IO) { specialtyDao.getById(id) }

            facultyEntity?.let { specialtyEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun saveList(specialties: List<Specialty>): Resource<Unit> {
        return Resource.tryWithSuspend {
            val specialtiesEntity = specialties.map { specialtyToEntityMapper.map(it) }

            withContext(Dispatchers.IO) { specialtyDao.save(specialtiesEntity) }
        }
    }

    override suspend fun clear(): Resource<Unit> {
        return Resource.tryWithSuspend {
            withContext(Dispatchers.IO) { specialtyDao.clear() }
        }
    }

}