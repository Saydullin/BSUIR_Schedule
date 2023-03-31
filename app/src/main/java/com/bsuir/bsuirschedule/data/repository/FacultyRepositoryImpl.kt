package com.bsuir.bsuirschedule.data.repository

import com.bsuir.bsuirschedule.api.RetrofitBuilder
import com.bsuir.bsuirschedule.api.services.GetFacultiesService
import com.bsuir.bsuirschedule.data.db.dao.FacultyDao
import com.bsuir.bsuirschedule.domain.models.Faculty
import com.bsuir.bsuirschedule.domain.repository.FacultyRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.StatusCode

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
                    errorType = StatusCode.SERVER_ERROR,
                    message = result.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = StatusCode.CONNECTION_ERROR,
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
                errorType = StatusCode.SERVER_ERROR,
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
                errorType = StatusCode.SERVER_ERROR,
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
                errorType = StatusCode.SERVER_ERROR,
                message = e.message
            )
        }
    }

}