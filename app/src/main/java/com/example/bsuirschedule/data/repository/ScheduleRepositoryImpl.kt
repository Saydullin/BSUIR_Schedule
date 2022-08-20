package com.example.bsuirschedule.data.repository

import android.database.sqlite.SQLiteException
import com.example.bsuirschedule.api.RetrofitBuilder
import com.example.bsuirschedule.api.services.GetGroupScheduleService
import com.example.bsuirschedule.data.db.dao.GroupScheduleDao
import com.example.bsuirschedule.domain.models.GroupSchedule
import com.example.bsuirschedule.domain.repository.ScheduleRepository
import com.example.bsuirschedule.domain.utils.Resource

class ScheduleRepositoryImpl(
    override val groupScheduleDao: GroupScheduleDao,
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

    override suspend fun getScheduleById(groupId: Int): Resource<GroupSchedule> {
        return try {
            val data = groupScheduleDao.getGroupScheduleById(groupId)
                ?: return Resource.Error(
                    errorType = Resource.DATABASE_NOT_FOUND_ERROR
                )
            Resource.Success(data.toGroupSchedule())
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

    override suspend fun saveSchedule(schedule: GroupSchedule): Resource<Unit> {
        return try {
            groupScheduleDao.saveSchedule(schedule.toGroupScheduleTable())
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
            groupScheduleDao.deleteGroupSchedule(groupName)
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
            groupScheduleDao.deleteEmployeeSchedule(employeeUrlId)
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

}


