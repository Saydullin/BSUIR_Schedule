package com.bsuir.bsuirschedule.domain.repository

import com.bsuir.bsuirschedule.data.db.dao.ScheduleDao
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.domain.utils.Resource

interface ScheduleRepository {

    val scheduleDao: ScheduleDao

    suspend fun getGroupScheduleAPI(groupName: String): Resource<GroupSchedule>

    suspend fun getEmployeeScheduleAPI(groupName: String): Resource<GroupSchedule>

    suspend fun getScheduleById(id: Int): Resource<Schedule>

    suspend fun saveSchedule(schedule: Schedule): Resource<Unit>

    suspend fun updateScheduleSettings(id: Int, newSettings: ScheduleSettings): Resource<Unit>

    suspend fun deleteGroupSchedule(groupName: String): Resource<Unit>

    suspend fun deleteEmployeeSchedule(employeeUrlId: String): Resource<Unit>

}