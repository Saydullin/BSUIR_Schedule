package by.devsgroup.domain.repository.schedule

import by.devsgroup.domain.model.schedule.template.ScheduleTemplate
import by.devsgroup.resource.Resource

interface ScheduleDatabaseRepository {

    suspend fun getGroupSchedule(groupName: String): Resource<ScheduleTemplate?>

    suspend fun getEmployeeSchedule(urlId: String): Resource<ScheduleTemplate?>

    suspend fun saveSchedule(scheduleTemplate: ScheduleTemplate): Resource<Unit>

    suspend fun clear(): Resource<Unit>

}