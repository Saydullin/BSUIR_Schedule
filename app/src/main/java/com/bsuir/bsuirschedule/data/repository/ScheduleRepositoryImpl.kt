package com.bsuir.bsuirschedule.data.repository

import android.database.sqlite.SQLiteException
import android.util.Log
import com.bsuir.bsuirschedule.api.RetrofitBuilder
import com.bsuir.bsuirschedule.api.services.GetGroupScheduleService
import com.bsuir.bsuirschedule.data.db.dao.ScheduleDao
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleLastUpdatedDate
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.google.gson.Gson

class ScheduleRepositoryImpl(
    override val scheduleDao: ScheduleDao,
) : ScheduleRepository {

    private val groupScheduleService = RetrofitBuilder.getInstance().create(GetGroupScheduleService::class.java)

    override suspend fun getGroupScheduleAPI(groupName: String): Resource<GroupSchedule> {

        return try {
            val result = groupScheduleService.getGroupSchedule(groupName)
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

    override suspend fun getEmployeeScheduleAPI(groupName: String): Resource<GroupSchedule> {

        return try {
            val result = groupScheduleService.getEmployeeSchedule(groupName)
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

    override suspend fun getEmployeeLastUpdatedDate(scheduleId: Int): Resource<ScheduleLastUpdatedDate> {

        return try {
            val result = groupScheduleService.getGroupLastUpdateDate(scheduleId)
            val data = result.body()
                ?: return Resource.Error(
                    errorType = Resource.SERVER_ERROR
                )
            return Resource.Success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.CONNECTION_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun getGroupLastUpdatedDate(scheduleId: Int): Resource<ScheduleLastUpdatedDate> {

        return try {
            val result = groupScheduleService.getGroupLastUpdateDate(scheduleId)
            val data = result.body()
                ?: return Resource.Error(
                    errorType = Resource.SERVER_ERROR
                )
            return Resource.Success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.CONNECTION_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun getScheduleById(id: Int): Resource<Schedule> {
        return try {
            Log.e("sady", "Data found3.1: id = $id")
            val data = scheduleDao.getScheduleById(id)
                ?: return Resource.Error(
                    errorType = Resource.DATABASE_NOT_FOUND_ERROR
                )
            Log.e("sady", "Data found3.2: $data")
            Resource.Success(data.toSchedule())
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun saveSchedule(schedule: Schedule): Resource<Unit> {
        return try {
            scheduleDao.saveSchedule(schedule.toScheduleTable())
            Resource.Success(null)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun updateScheduleSettings(
        id: Int,
        newSettings: ScheduleSettings
    ): Resource<Unit> {
        return try {
            scheduleDao.updateScheduleSettings(id, Gson().toJson(newSettings))
            Resource.Success(null)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun deleteScheduleById(scheduleId: Int): Resource<Unit> {
        return try {
            scheduleDao.deleteScheduleById(scheduleId)
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

}


