package com.saydullin.faculty.repository

import by.devsgroup.domain.model.faculty.Faculty
import by.devsgroup.domain.repository.faculty.FacultyServerRepository
import by.devsgroup.resource.Resource
import com.saydullin.faculty.mapper.FacultyDataToDomainMapper
import com.saydullin.faculty.server.service.FacultyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FacultyServerRepositoryImpl @Inject constructor(
    private val facultyService: FacultyService,
    private val facultyDataToDomainMapper: FacultyDataToDomainMapper,
): FacultyServerRepository {

    override suspend fun getAllFaculties(): Resource<List<Faculty>> {
        return Resource.tryWithSuspend {
            val faculties = withContext(Dispatchers.IO) {
                facultyService.getAllFaculties()
            }

            faculties.map { facultyDataToDomainMapper.map(it) }
        }
    }

}


