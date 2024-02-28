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
import com.bsuir.bsuirschedule.domain.utils.StatusCode
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
                Log.e("sady", "getGroupScheduleAPI custom error")
                Resource.Error(
                    statusCode = StatusCode.SERVER_ERROR,
                    message = result.message()
                )
            }
        } catch (e: Exception) {
            Log.e("sady", "getGroupScheduleAPI")
            Log.e("sady", e.printStackTrace().toString())
            e.printStackTrace()
            Resource.Error(
                statusCode = StatusCode.CONNECTION_ERROR,
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
                    statusCode = StatusCode.SERVER_ERROR,
                    message = result.message()
                )
            }
        } catch (e: Exception) {
            Log.e("sady", "getEmployeeScheduleAPI")
            e.printStackTrace()
            Resource.Error(
                statusCode = StatusCode.CONNECTION_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun getEmployeeLastUpdatedDate(scheduleId: Int): Resource<ScheduleLastUpdatedDate> {

        return try {
            val result = groupScheduleService.getGroupLastUpdateDate(scheduleId)
            val data = result.body()
                ?: return Resource.Error(
                    statusCode = StatusCode.SERVER_ERROR
                )
            return Resource.Success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                statusCode = StatusCode.CONNECTION_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun getGroupLastUpdatedDate(scheduleId: Int): Resource<ScheduleLastUpdatedDate> {

        return try {
            val result = groupScheduleService.getGroupLastUpdateDate(scheduleId)
            val data = result.body()
                ?: return Resource.Error(
                    statusCode = StatusCode.SERVER_ERROR
                )
            return Resource.Success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                statusCode = StatusCode.CONNECTION_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun getScheduleById(id: Int): Resource<Schedule> {
        return try {
            val data = scheduleDao.getScheduleById(id)
                ?: return Resource.Error(
                    statusCode = StatusCode.DATABASE_NOT_FOUND_ERROR
                )
            Resource.Success(data.toSchedule())
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Resource.Error(
                statusCode = StatusCode.DATABASE_ERROR,
                message = e.message
            )
        } catch (e: Exception) {
            Log.e("sady", "getScheduleById ${e.message}")
            e.printStackTrace()
            Resource.Error(
                statusCode = StatusCode.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun saveSchedule(schedule: Schedule): Resource<Unit> {
        return try {
            scheduleDao.saveSchedule(schedule.toScheduleTable())
            Resource.Success(null)
        } catch (e: Exception) {
            Log.e("sady", "saveSchedule")
            Log.e("sady", e.message.toString())
            e.printStackTrace()
            Resource.Error(
                statusCode = StatusCode.DATABASE_ERROR,
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
            Log.e("sady", "updateScheduleSettings")
            e.printStackTrace()
            Resource.Error(
                statusCode = StatusCode.DATABASE_ERROR,
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
                statusCode = StatusCode.DATABASE_ERROR,
                message = e.message
            )
        }
    }

}


