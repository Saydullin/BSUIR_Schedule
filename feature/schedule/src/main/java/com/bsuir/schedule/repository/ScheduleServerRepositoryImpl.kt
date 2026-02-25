package com.bsuir.schedule.repository

import com.bsuir.domain.model.schedule.template.ScheduleTemplate
import com.bsuir.domain.repository.schedule.ScheduleServerRepository
import com.bsuir.resource.Resource
import com.bsuir.schedule.mapper.ScheduleDataToDomainMapper
import com.bsuir.schedule.server.service.ScheduleService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScheduleServerRepositoryImpl @Inject constructor(
    private val scheduleService: ScheduleService,
    private val scheduleDataToDomainMapper: ScheduleDataToDomainMapper,
): ScheduleServerRepository {

    override suspend fun getGroupSchedule(groupName: String): Resource<ScheduleTemplate> {
        return Resource.tryWithSuspend {
            val scheduleData = withContext(Dispatchers.IO) {
                scheduleService.getGroupSchedule(groupName)
            } ?: throw Exception("Not found")

            scheduleDataToDomainMapper.map(scheduleData)
        }
    }

    override suspend fun getEmployeeSchedule(urlId: String): Resource<ScheduleTemplate> {
        return Resource.tryWithSuspend {
            val scheduleData = withContext(Dispatchers.IO) {
                scheduleService.getEmployeeSchedule(urlId)
            } ?: throw Exception("Not found")

            scheduleDataToDomainMapper.map(scheduleData)
        }
    }

}