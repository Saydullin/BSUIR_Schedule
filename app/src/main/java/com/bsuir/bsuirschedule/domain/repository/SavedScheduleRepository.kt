package com.bsuir.bsuirschedule.domain.repository

import com.bsuir.bsuirschedule.data.db.dao.SavedScheduleDao
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.utils.Resource

interface SavedScheduleRepository {

    val savedScheduleDao: SavedScheduleDao

    suspend fun getSavedSchedules(): Resource<ArrayList<SavedSchedule>>

    suspend fun saveSchedule(schedule: SavedSchedule): Resource<Unit>

    suspend fun getSavedScheduleById(scheduleId: Int): Resource<SavedSchedule>

    suspend fun filterByKeywordASC(title: String): Resource<ArrayList<SavedSchedule>>

    suspend fun filterByKeywordDESC(title: String): Resource<ArrayList<SavedSchedule>>

    suspend fun deleteSchedule(schedule: SavedSchedule): Resource<Unit>

}


