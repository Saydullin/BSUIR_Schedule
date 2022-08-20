package com.example.bsuirschedule.data.repository

import com.example.bsuirschedule.api.RetrofitBuilder
import com.example.bsuirschedule.api.services.GetCurrentWeekService
import com.example.bsuirschedule.data.db.dao.CurrentWeekDao
import com.example.bsuirschedule.domain.models.CurrentWeek
import com.example.bsuirschedule.domain.repository.CurrentWeekRepository
import com.example.bsuirschedule.domain.utils.Resource
import java.util.*

class CurrentWeekRepositoryImpl(private val currentWeekDao: CurrentWeekDao) : CurrentWeekRepository {

    override suspend fun getCurrentWeekAPI(): Resource<CurrentWeek> {
        val currentWeekService = RetrofitBuilder.getInstance().create(GetCurrentWeekService::class.java)

        return try {
            val result = currentWeekService.getCurrentWeek()
            val data = result.body()
            return if (result.isSuccessful && data != null) {
                Resource.Success(CurrentWeek(week = data, time = Date().time))
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

    override suspend fun getCurrentWeek(): Resource<CurrentWeek> {

        return try {
            val data = currentWeekDao.getCurrentWeek()
                ?: return Resource.Error(
                    errorType = Resource.DATABASE_NOT_FOUND_ERROR
                )
            Resource.Success(data.toCurrentWeek())
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun saveCurrentWeek(currentWeek: CurrentWeek): Resource<Unit> {
        return try {
            currentWeekDao.saveCurrentWeek(currentWeek.toCurrentWeekTable())
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

}