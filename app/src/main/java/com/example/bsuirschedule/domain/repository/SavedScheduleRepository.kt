package com.example.bsuirschedule.domain.repository

import com.example.bsuirschedule.data.db.dao.SavedScheduleDao
import com.example.bsuirschedule.domain.models.SavedSchedule
import com.example.bsuirschedule.domain.utils.Resource

interface SavedScheduleRepository {

    val savedScheduleDao: SavedScheduleDao

    suspend fun getSavedSchedules(): Resource<ArrayList<SavedSchedule>>

    suspend fun saveSchedule(schedule: SavedSchedule): Resource<Unit>

    suspend fun getSavedScheduleById(scheduleId: Int): Resource<SavedSchedule>

    suspend fun filterByKeywordASC(title: String): Resource<ArrayList<SavedSchedule>>

    suspend fun filterByKeywordDESC(title: String): Resource<ArrayList<SavedSchedule>>

    suspend fun deleteSchedule(schedule: SavedSchedule): Resource<Unit>

}


