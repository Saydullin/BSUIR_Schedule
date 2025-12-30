package by.devsgroup.schedule.repository

import by.devsgroup.database.schedule.dao.ScheduleDao
import by.devsgroup.domain.model.schedule.template.ScheduleTemplate
import by.devsgroup.domain.repository.schedule.ScheduleDatabaseRepository
import by.devsgroup.resource.Resource
import by.devsgroup.schedule.mapper.ScheduleToEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScheduleDatabaseRepositoryImpl @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val scheduleToEntityMapper: ScheduleToEntityMapper,
): ScheduleDatabaseRepository {

    override suspend fun getGroupSchedule(groupName: String): Resource<ScheduleTemplate?> {
        TODO("Not yet implemented")
    }

    override suspend fun getEmployeeSchedule(urlId: String): Resource<ScheduleTemplate?> {
        TODO("Not yet implemented")
    }

    override suspend fun saveSchedule(scheduleTemplate: ScheduleTemplate): Resource<Unit> {
        return Resource.tryWithSuspend {
            val scheduleEntity = scheduleToEntityMapper.map(scheduleTemplate)

            withContext(Dispatchers.IO) { scheduleDao.clearAndSave(scheduleEntity) }
        }
    }

    override suspend fun clear(): Resource<Unit> {
        return Resource.tryWithSuspend {
            withContext(Dispatchers.IO) { scheduleDao.clear() }
        }
    }

}


