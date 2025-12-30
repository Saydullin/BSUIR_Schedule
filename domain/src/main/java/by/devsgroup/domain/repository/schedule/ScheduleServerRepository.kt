package by.devsgroup.domain.repository.schedule

import by.devsgroup.domain.model.schedule.template.ScheduleTemplate
import by.devsgroup.resource.Resource

interface ScheduleServerRepository {

    suspend fun getGroupSchedule(groupName: String): Resource<ScheduleTemplate>

    suspend fun getEmployeeSchedule(urlId: String): Resource<ScheduleTemplate>

}