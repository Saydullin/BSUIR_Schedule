package com.bsuir.bsuirschedule.domain.repository

import com.bsuir.bsuirschedule.data.db.dao.GroupScheduleDao
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.utils.Resource

interface ScheduleRepository {

    val groupScheduleDao: GroupScheduleDao

    suspend fun getScheduleAPI(groupName: String): Resource<GroupSchedule>

    suspend fun getEmployeeScheduleAPI(groupName: String): Resource<GroupSchedule>

    suspend fun getScheduleById(groupId: Int): Resource<GroupSchedule>

    suspend fun saveSchedule(schedule: GroupSchedule): Resource<Unit>

    suspend fun deleteGroupSchedule(groupName: String): Resource<Unit>

    suspend fun deleteEmployeeSchedule(employeeUrlId: String): Resource<Unit>

}