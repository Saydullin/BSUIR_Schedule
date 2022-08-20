package com.example.bsuirschedule.data.repository

import com.example.bsuirschedule.api.RetrofitBuilder
import com.example.bsuirschedule.api.services.GetSpecialitiesService
import com.example.bsuirschedule.data.db.dao.SpecialityDao
import com.example.bsuirschedule.domain.models.Speciality
import com.example.bsuirschedule.domain.repository.SpecialityRepository
import com.example.bsuirschedule.domain.utils.Resource

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

    override suspend fun getSpecialities(): Resource<ArrayList<Speciality>> {

        return try {
            val result = specialityDao.getSpecialities()
            val specialitiesList = result.map { it.toSpeciality() } as ArrayList<Speciality>
            Resource.Success(specialitiesList)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
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
                errorType = Resource.DATABASE_ERROR,
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
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

}