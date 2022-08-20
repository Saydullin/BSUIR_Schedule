package com.example.bsuirschedule.data.repository

import com.example.bsuirschedule.data.db.dao.SavedScheduleDao
import com.example.bsuirschedule.domain.models.SavedSchedule
import com.example.bsuirschedule.domain.repository.SavedScheduleRepository
import com.example.bsuirschedule.domain.utils.Resource

class SavedScheduleRepositoryImpl(override val savedScheduleDao: SavedScheduleDao) : SavedScheduleRepository {

    override suspend fun getSavedSchedules(): Resource<ArrayList<SavedSchedule>> {
        return try {
            val data = savedScheduleDao.getSavedSchedules().map { it.toSavedSchedule() } as ArrayList<SavedSchedule>
            Resource.Success(data)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun saveSchedule(schedule: SavedSchedule): Resource<Unit> {
        return try {
            savedScheduleDao.saveSchedule(schedule.toSavedScheduleTable())
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun getSavedScheduleById(scheduleId: Int): Resource<SavedSchedule> {
        return try {
            val data = savedScheduleDao.getSavedScheduleById(scheduleId)
                ?: return Resource.Error(
                    errorType = Resource.DATABASE_NOT_FOUND_ERROR,
                )
            Resource.Success(data.toSavedSchedule())
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun filterByKeywordASC(title: String): Resource<ArrayList<SavedSchedule>> {

        return try {
            val result = savedScheduleDao.filterByKeywordASC("%${title}%")
            val scheduleList = result.map { it.toSavedSchedule() } as ArrayList<SavedSchedule>
            Resource.Success(scheduleList)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun filterByKeywordDESC(title: String): Resource<ArrayList<SavedSchedule>> {

        return try {
            val result = savedScheduleDao.filterByKeywordDESC("%${title}%")
            val scheduleList = result.map { it.toSavedSchedule() } as ArrayList<SavedSchedule>
            Resource.Success(scheduleList)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

    override suspend fun deleteSchedule(schedule: SavedSchedule): Resource<Unit> {
        return try {
            savedScheduleDao.deleteSchedule(schedule.toSavedScheduleTable())
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATABASE_ERROR,
                message = e.message
            )
        }
    }

}


