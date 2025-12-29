package by.devsgroup.domain.repository.schedule

import by.devsgroup.domain.model.schedule.Schedule
import by.devsgroup.resource.Resource

interface ScheduleServerRepository {

    suspend fun getGroupSchedule(groupName: String): Resource<Schedule>

    suspend fun getEmployeeSchedule(urlId: String): Resource<Schedule>

}