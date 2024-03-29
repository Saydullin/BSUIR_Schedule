package com.bsuir.bsuirschedule.domain.repository

import com.bsuir.bsuirschedule.data.db.dao.ScheduleDao
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleLastUpdatedDate
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.domain.utils.Resource

interface ScheduleRepository {

    val scheduleDao: ScheduleDao

    suspend fun getGroupScheduleAPI(groupName: String): Resource<GroupSchedule>

    suspend fun getEmployeeScheduleAPI(groupName: String): Resource<GroupSchedule>

    suspend fun getEmployeeLastUpdatedDate(scheduleId: Int): Resource<ScheduleLastUpdatedDate>

    suspend fun getGroupLastUpdatedDate(scheduleId: Int): Resource<ScheduleLastUpdatedDate>

    suspend fun getScheduleById(id: Int): Resource<Schedule>

    suspend fun saveSchedule(schedule: Schedule): Resource<Unit>

    suspend fun updateScheduleSettings(id: Int, newSettings: ScheduleSettings): Resource<Unit>

    suspend fun deleteScheduleById(scheduleId: Int): Resource<Unit>

}


