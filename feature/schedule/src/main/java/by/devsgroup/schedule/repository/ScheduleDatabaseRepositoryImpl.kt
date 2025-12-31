package by.devsgroup.schedule.repository

import by.devsgroup.database.schedule.dao.ScheduleDao
import by.devsgroup.database.schedule.dao.ScheduleDayDao
import by.devsgroup.database.schedule.dao.ScheduleLessonDao
import by.devsgroup.database.schedule.entity.ScheduleDayEntity
import by.devsgroup.domain.model.schedule.full.FullSchedule
import by.devsgroup.domain.model.schedule.preview.PreviewSchedule
import by.devsgroup.domain.repository.schedule.ScheduleDatabaseRepository
import by.devsgroup.resource.Resource
import by.devsgroup.schedule.mapper.ScheduleLessonTemplateToEntityMapper
import by.devsgroup.schedule.mapper.ScheduleToEntityMapper
import by.devsgroup.schedule.mapper.context.ScheduleLessonContext
import by.devsgroup.schedule.mapper.entityToDomain.ScheduleEntityToDomainMapper
import by.devsgroup.schedule.mapper.entityToDomain.ScheduleWithDaysEntityToDomainMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class ScheduleDatabaseRepositoryImpl @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val scheduleDayDao: ScheduleDayDao,
    private val scheduleLessonDao: ScheduleLessonDao,
    private val scheduleToEntityMapper: ScheduleToEntityMapper,
    private val scheduleEntityToDomainMapper: ScheduleEntityToDomainMapper,
    private val scheduleWithDaysEntityToDomainMapper: ScheduleWithDaysEntityToDomainMapper,
    private val scheduleLessonTemplateToEntityMapper: ScheduleLessonTemplateToEntityMapper,
): ScheduleDatabaseRepository {

    override suspend fun getScheduleById(id: Long): Resource<PreviewSchedule?> {
        return Resource.tryWithSuspend {
            val schedule = withContext(Dispatchers.IO) { scheduleDao.getSchedule(id) }
                ?: throw Exception("not found")

            scheduleEntityToDomainMapper.map(schedule)
        }
    }

    override suspend fun getFullScheduleById(id: Long): Resource<FullSchedule?> {
        return Resource.tryWithSuspend {
            val schedule = withContext(Dispatchers.IO) { scheduleDao.getFullSchedule(id) }
                ?: throw Exception("not found")

            scheduleWithDaysEntityToDomainMapper.map(schedule)
        }
    }

    override suspend fun saveSchedule(schedule: FullSchedule): Resource<Unit> {
        return Resource.tryWithSuspend {
            val scheduleEntity = scheduleToEntityMapper.map(schedule)

            withContext(Dispatchers.IO) {
                scheduleDao.save(scheduleEntity)
            }

            val scheduleId = scheduleEntity.scheduleId

            schedule.schedules?.forEach { day ->
                val dayId = UUID.randomUUID().toString()

                val dayEntity = ScheduleDayEntity(
                    scheduleId = scheduleId,
                    dayId = dayId,
                    dateMillis = day.date,
                    week = day.week,
                )

                withContext(Dispatchers.IO) {
                    scheduleDayDao.save(dayEntity)
                }

                val lessonEntities = day.lessons?.map { lesson ->
                    val lessonId = UUID.randomUUID().toString()

                    val lessonMapperContext = ScheduleLessonContext(
                        scheduleId = scheduleId,
                        lessonId = lessonId,
                        dayId = dayId,
                    )

                    scheduleLessonTemplateToEntityMapper.map(lesson, lessonMapperContext)
                }

                withContext(Dispatchers.IO) {
                    lessonEntities?.let { scheduleLessonDao.save(it) }
                }
            }
        }
    }

    override suspend fun clear(): Resource<Unit> {
        return Resource.tryWithSuspend {
            withContext(Dispatchers.IO) { scheduleDao.clear() }
        }
    }

}


