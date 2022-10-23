package com.bsuir.bsuirschedule.data.repository

import android.database.sqlite.SQLiteException
import com.bsuir.bsuirschedule.api.RetrofitBuilder
import com.bsuir.bsuirschedule.api.services.GetGroupScheduleService
import com.bsuir.bsuirschedule.data.db.dao.ScheduleDao
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.google.gson.Gson

class ScheduleRepositoryImpl(
    override val scheduleDao: ScheduleDao,
) : ScheduleRepository {

    override suspend fun getScheduleAPI(groupName: String): Resource<GroupSchedule> {
        val groupScheduleService = RetrofitBuilder.getInstance().create(GetGroupScheduleService::class.java)

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
        val groupScheduleService = RetrofitBuilder.getInstance().create(GetGroupScheduleService::class.java)

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

    override suspend fun getScheduleById(id: Int): Resource<Schedule> {
        return try {
            val data = scheduleDao.getGroupScheduleById(id)
                ?: return Resource.Error(
                    errorType = Resource.DATABASE_NOT_FOUND_ERROR
                )
            Resource.Success(data.toSchedule())
        } catch (e: SQLiteException) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        } catch (e: Exception) {
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

    override suspend fun deleteGroupSchedule(groupName: String): Resource<Unit> {
        return try {
            scheduleDao.deleteGroupSchedule(groupName)
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun deleteEmployeeSchedule(employeeUrlId: String): Resource<Unit> {
        return try {
            scheduleDao.deleteEmployeeSchedule(employeeUrlId)
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

}


