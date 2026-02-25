package com.bsuir.domain.repository.schedule

import com.bsuir.domain.model.schedule.full.FullSchedule
import com.bsuir.domain.model.schedule.preview.PreviewSchedule
import com.bsuir.resource.Resource

interface ScheduleDatabaseRepository {

    suspend fun getAllPreviewSchedules(): Resource<List<PreviewSchedule>>

    suspend fun getScheduleById(id: Long): Resource<PreviewSchedule?>

    suspend fun getScheduleIdByEmployeeId(employeeId: Long): Resource<Long>

    suspend fun getScheduleIdByGroupId(groupId: Long): Resource<Long>

    suspend fun getFullScheduleById(id: Long): Resource<FullSchedule?>

    suspend fun saveSchedule(schedule: FullSchedule): Resource<Unit>

    suspend fun deleteScheduleByEmployeeId(employeeId: Long): Resource<Unit>

    suspend fun deleteScheduleByGroupId(groupId: Long): Resource<Unit>

    suspend fun clear(): Resource<Unit>

}