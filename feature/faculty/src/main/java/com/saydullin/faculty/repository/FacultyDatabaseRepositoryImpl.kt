package com.saydullin.faculty.repository

import com.bsuir.database.faculty.dao.FacultyDao
import com.bsuir.domain.model.faculty.Faculty
import com.bsuir.domain.repository.faculty.FacultyDatabaseRepository
import com.bsuir.resource.Resource
import com.saydullin.faculty.mapper.FacultyEntityToDomainMapper
import com.saydullin.faculty.mapper.FacultyToEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FacultyDatabaseRepositoryImpl @Inject constructor(
    private val facultyDao: FacultyDao,
    private val facultyToEntityMapper: FacultyToEntityMapper,
    private val facultyEntityToDomainMapper: FacultyEntityToDomainMapper,
): FacultyDatabaseRepository {

    override suspend fun getById(id: Long): Resource<Faculty?> {
        return Resource.tryWithSuspend {
            val facultyEntity = withContext(Dispatchers.IO) { facultyDao.getById(id) }

            facultyEntity?.let { facultyEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun saveList(faculties: List<Faculty>): Resource<Unit> {
        return Resource.tryWithSuspend {
            val facultiesEntity = faculties.map { facultyToEntityMapper.map(it) }

            withContext(Dispatchers.IO) { facultyDao.save(facultiesEntity) }
        }
    }

    override suspend fun clear(): Resource<Unit> {
        return Resource.tryWithSuspend {
            withContext(Dispatchers.IO) { facultyDao.clear() }
        }
    }

}