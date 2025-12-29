package by.devsgroup.schedule.repository

import by.devsgroup.database.schedule.dao.ScheduleDao
import by.devsgroup.domain.model.schedule.Schedule
import by.devsgroup.domain.repository.schedule.ScheduleDatabaseRepository
import by.devsgroup.resource.Resource
import javax.inject.Inject

class ScheduleDatabaseRepositoryImpl @Inject constructor(
    private val scheduleDao: ScheduleDao
): ScheduleDatabaseRepository {

    override suspend fun getGroupSchedule(groupName: String): Resource<Schedule?> {
        TODO("Not yet implemented")
    }

    override suspend fun getEmployeeSchedule(urlId: String): Resource<Schedule?> {
        TODO("Not yet implemented")
    }

    override suspend fun saveSchedule(schedule: Schedule): Resource<Unit> {
        return Resource.tryWithSuspend {
            scheduleDao.clearAndSave(schedule)
        }
    }

    override suspend fun clear(): Resource<Unit> {
        TODO("Not yet implemented")
    }

}


