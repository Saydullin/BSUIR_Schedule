package by.devsgroup.schedule.repository

import by.devsgroup.domain.model.schedule.Schedule
import by.devsgroup.domain.repository.schedule.ScheduleServerRepository
import by.devsgroup.resource.Resource
import by.devsgroup.schedule.server.service.ScheduleService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScheduleServerRepositoryImpl @Inject constructor(
    private val scheduleService: ScheduleService
): ScheduleServerRepository {

    override suspend fun getGroupSchedule(groupName: String): Resource<Schedule?> {
        println("getGroupSchedule $groupName")
        try {
            val schedule = withContext(Dispatchers.IO) {
                scheduleService.getGroupSchedule(groupName)
            }

            println("schedule $schedule")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Resource.Success(null)
    }

    override suspend fun getEmployeeSchedule(urlId: String): Resource<Schedule> {
        TODO("Not yet implemented")
    }

}