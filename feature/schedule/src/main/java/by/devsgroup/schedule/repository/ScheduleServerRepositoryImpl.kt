package by.devsgroup.schedule.repository

import by.devsgroup.domain.model.schedule.Schedule
import by.devsgroup.domain.repository.schedule.ScheduleServerRepository
import by.devsgroup.resource.Resource
import by.devsgroup.schedule.mapper.ScheduleDataToDomainMapper
import by.devsgroup.schedule.server.service.ScheduleService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScheduleServerRepositoryImpl @Inject constructor(
    private val scheduleService: ScheduleService,
    private val scheduleDataToDomainMapper: ScheduleDataToDomainMapper,
): ScheduleServerRepository {

    override suspend fun getGroupSchedule(groupName: String): Resource<Schedule> {
        return Resource.tryWithSuspend {
            val scheduleData = withContext(Dispatchers.IO) {
                scheduleService.getGroupSchedule(groupName)
            } ?: throw Exception("Not found")

            scheduleDataToDomainMapper.map(scheduleData)
        }
    }

    override suspend fun getEmployeeSchedule(urlId: String): Resource<Schedule> {
        TODO("Not yet implemented")
    }

}