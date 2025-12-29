package by.devsgroup.domain.repository.schedule

import by.devsgroup.domain.model.schedule.Schedule
import by.devsgroup.resource.Resource

interface ScheduleDatabaseRepository {

    suspend fun getGroupSchedule(groupName: String): Resource<Schedule?>

    suspend fun getEmployeeSchedule(urlId: String): Resource<Schedule?>

    suspend fun saveSchedule(schedule: Schedule): Resource<Unit>

    suspend fun clear(): Resource<Unit>

}