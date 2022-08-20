package com.example.bsuirschedule.data.repository

import com.example.bsuirschedule.api.RetrofitBuilder
import com.example.bsuirschedule.api.services.GetFacultiesService
import com.example.bsuirschedule.data.db.dao.FacultyDao
import com.example.bsuirschedule.domain.models.Faculty
import com.example.bsuirschedule.domain.repository.FacultyRepository
import com.example.bsuirschedule.domain.utils.Resource

class FacultyRepositoryImpl(override val facultyDao: FacultyDao) : FacultyRepository {

    override suspend fun getFacultiesAPI(): Resource<ArrayList<Faculty>> {
        val facultiesService = RetrofitBuilder.getInstance().create(GetFacultiesService::class.java)

        return try {
            val result = facultiesService.getFaculties()
            val data = result.body()
            return if (result.isSuccessful && data != null) {
                Resource.Success(data)
            } else {
                Resource.Error(
                    errorType = Resource.SERVER_ERROR,
                    message = result.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.CONNECTION_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun getFaculties(): Resource<ArrayList<Faculty>> {

        return try {
            val result = facultyDao.getFaculties()
            val facultiesList = result.map { it.toFaculty() } as ArrayList<Faculty>
            Resource.Success(facultiesList)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.SERVER_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun getFacultyById(facultyId: Int): Resource<Faculty> {

        return try {
            val result = facultyDao.getFacultyById(facultyId)
            Resource.Success(result.toFaculty())
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.SERVER_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun saveFaculties(faculties: ArrayList<Faculty>): Resource<Unit> {

        return try {
            val facultiesList = faculties.map { it.toFacultyTable() }
            facultyDao.saveFaculties(facultiesList)
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.SERVER_ERROR,
                message = e.message
            )
        }
    }

}