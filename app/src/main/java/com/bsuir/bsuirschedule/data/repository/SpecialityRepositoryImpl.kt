package com.bsuir.bsuirschedule.data.repository

import com.bsuir.bsuirschedule.api.RetrofitBuilder
import com.bsuir.bsuirschedule.api.services.GetSpecialitiesService
import com.bsuir.bsuirschedule.data.db.dao.SpecialityDao
import com.bsuir.bsuirschedule.domain.models.Speciality
import com.bsuir.bsuirschedule.domain.repository.SpecialityRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.StatusCode

class SpecialityRepositoryImpl(override val specialityDao: SpecialityDao) : SpecialityRepository {

    override suspend fun getSpecialitiesAPI(): Resource<ArrayList<Speciality>> {
        val specialitiesService = RetrofitBuilder.getInstance().create(GetSpecialitiesService::class.java)

        return try {
            val result = specialitiesService.getSpecialities()
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

    override suspend fun getSpecialities(): Resource<ArrayList<Speciality>> {

        return try {
            val result = specialityDao.getSpecialities()
            val specialitiesList = result.map { it.toSpeciality() } as ArrayList<Speciality>
            Resource.Success(specialitiesList)
        } catch (e: Exception) {
            Resource.Error(
                errorType = StatusCode.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun getSpecialityById(specialityId: Int): Resource<Speciality> {

        return try {
            val result = specialityDao.getSpecialitiesById(specialityId)
            Resource.Success(result.toSpeciality())
        } catch (e: Exception) {
            Resource.Error(
                errorType = StatusCode.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun saveSpecialities(specialities: ArrayList<Speciality>): Resource<Unit> {

        return try {
            val specialitiesList = specialities.map { it.toSpecialityTable() }
            specialityDao.saveSpecialities(specialitiesList)
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(
                errorType = StatusCode.DATABASE_ERROR,
                message = e.message
            )
        }
    }

}