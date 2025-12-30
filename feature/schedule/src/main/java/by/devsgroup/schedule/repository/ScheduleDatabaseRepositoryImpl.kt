package by.devsgroup.schedule.repository

import by.devsgroup.database.schedule.dao.ScheduleDao
import by.devsgroup.database.schedule.dao.ScheduleLessonDao
import by.devsgroup.domain.model.schedule.template.ScheduleTemplate
import by.devsgroup.domain.repository.schedule.ScheduleDatabaseRepository
import by.devsgroup.resource.Resource
import by.devsgroup.schedule.mapper.ScheduleLessonTemplateToEntityMapper
import by.devsgroup.schedule.mapper.ScheduleToEntityMapper
import by.devsgroup.schedule.mapper.context.ScheduleLessonContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class ScheduleDatabaseRepositoryImpl @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val scheduleLessonDao: ScheduleLessonDao,
    private val scheduleToEntityMapper: ScheduleToEntityMapper,
    private val scheduleLessonTemplateToEntityMapper: ScheduleLessonTemplateToEntityMapper,
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

            withContext(Dispatchers.IO) {
                scheduleDao.save(scheduleEntity)
            }

            val scheduleId = scheduleEntity.scheduleId

            println("scheduleTemplate.schedules ${scheduleTemplate.schedules?.size}")

            val lessonEntities = scheduleTemplate.schedules?.map { lesson ->
                val lessonId = UUID.randomUUID().toString()

                val lessonMapperContext = ScheduleLessonContext(
                    scheduleId = scheduleId,
                    lessonId = lessonId,
                )

                scheduleLessonTemplateToEntityMapper.map(lesson, lessonMapperContext)
            }

            withContext(Dispatchers.IO) {
                lessonEntities?.let { scheduleLessonDao.save(it) }
            }
        }
    }

    override suspend fun clear(): Resource<Unit> {
        return Resource.tryWithSuspend {
            withContext(Dispatchers.IO) { scheduleDao.clear() }
        }
    }

}


