package com.bsuir.schedule.repository

import com.bsuir.database.schedule.dao.ScheduleDao
import com.bsuir.database.schedule.dao.ScheduleDayDao
import com.bsuir.database.schedule.dao.ScheduleEmployeeDao
import com.bsuir.database.schedule.dao.ScheduleGroupDao
import com.bsuir.database.schedule.dao.ScheduleLessonDao
import com.bsuir.database.schedule.dao.ScheduleLessonEmployeeDao
import com.bsuir.database.schedule.dao.ScheduleLessonGroupDao
import com.bsuir.database.schedule.entity.ScheduleDayEntity
import com.bsuir.domain.model.schedule.full.FullSchedule
import com.bsuir.domain.model.schedule.preview.PreviewSchedule
import com.bsuir.domain.repository.schedule.ScheduleDatabaseRepository
import com.bsuir.resource.Resource
import com.bsuir.schedule.mapper.ScheduleEmployeeToEntityMapper
import com.bsuir.schedule.mapper.ScheduleGroupToEntityMapper
import com.bsuir.schedule.mapper.ScheduleLessonEmployeeToEntityMapper
import com.bsuir.schedule.mapper.ScheduleLessonGroupToEntityMapper
import com.bsuir.schedule.mapper.ScheduleLessonTemplateToEntityMapper
import com.bsuir.schedule.mapper.ScheduleToEntityMapper
import com.bsuir.schedule.mapper.context.ScheduleLessonToEntityMapperContext
import com.bsuir.schedule.mapper.entityToDomain.ScheduleEntityToDomainMapper
import com.bsuir.schedule.mapper.entityToDomain.ScheduleWithDaysEntityToDomainMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class ScheduleDatabaseRepositoryImpl @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val scheduleDayDao: ScheduleDayDao,
    private val scheduleGroupDao: ScheduleGroupDao,
    private val scheduleLessonDao: ScheduleLessonDao,
    private val scheduleEmployeeDao: ScheduleEmployeeDao,
    private val scheduleLessonGroupDao: ScheduleLessonGroupDao,
    private val scheduleToEntityMapper: ScheduleToEntityMapper,
    private val scheduleLessonEmployeeDao: ScheduleLessonEmployeeDao,
    private val scheduleGroupToEntityMapper: ScheduleGroupToEntityMapper,
    private val scheduleEntityToDomainMapper: ScheduleEntityToDomainMapper,
    private val scheduleEmployeeToEntityMapper: ScheduleEmployeeToEntityMapper,
    private val scheduleLessonGroupToEntityMapper: ScheduleLessonGroupToEntityMapper,
    private val scheduleWithDaysEntityToDomainMapper: ScheduleWithDaysEntityToDomainMapper,
    private val scheduleLessonTemplateToEntityMapper: ScheduleLessonTemplateToEntityMapper,
    private val scheduleLessonEmployeeToEntityMapper: ScheduleLessonEmployeeToEntityMapper,
): ScheduleDatabaseRepository {

    override suspend fun getAllPreviewSchedules(): Resource<List<PreviewSchedule>> {
        return Resource.tryWithSuspend {
            val scheduleList = withContext(Dispatchers.IO) { scheduleDao.getAllSchedules() }

            scheduleList.map { scheduleEntityToDomainMapper.map(it) }
        }
    }

    override suspend fun getScheduleById(id: Long): Resource<PreviewSchedule?> {
        return Resource.tryWithSuspend {
            val schedule = withContext(Dispatchers.IO) { scheduleDao.getSchedule(id) }
                ?: throw Exception("not found")

            scheduleEntityToDomainMapper.map(schedule)
        }
    }

    override suspend fun getScheduleIdByEmployeeId(employeeId: Long): Resource<Long> {
        return Resource.tryWithSuspend {
            withContext(Dispatchers.IO) {
                scheduleDao.getScheduleIdByEmployeeId(employeeId)
            } ?: throw Exception("not found")
        }
    }

    override suspend fun getScheduleIdByGroupId(groupId: Long): Resource<Long> {
        return Resource.tryWithSuspend {
            withContext(Dispatchers.IO) {
                scheduleDao.getScheduleIdByGroupId(groupId)
            } ?: throw Exception("not found")
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

            val scheduleEmployeeEntity = schedule.employee?.let { scheduleEmployeeToEntityMapper.map(it, scheduleId) }
            val scheduleGroupEntity = schedule.group?.let { scheduleGroupToEntityMapper.map(it, scheduleId) }

            scheduleEmployeeEntity?.let {
                withContext(Dispatchers.IO) { scheduleEmployeeDao.save(it) }
            }
            scheduleGroupEntity?.let {
                withContext(Dispatchers.IO) { scheduleGroupDao.save(it) }
            }

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

                    val lessonMapperContext = ScheduleLessonToEntityMapperContext(
                        scheduleId = scheduleId,
                        lessonId = lessonId,
                        dayId = dayId,
                    )

                    val lessonEmployeeEntityList = lesson.employees?.map {
                        scheduleLessonEmployeeToEntityMapper.map(it, lessonId)
                    }

                    val lessonGroupEntityList = lesson.studentGroups?.map {
                        scheduleLessonGroupToEntityMapper.map(it, lessonId)
                    }

                    lessonEmployeeEntityList?.let { lessonEmployeesList ->
                        withContext(Dispatchers.IO) {
                            scheduleLessonEmployeeDao.save(lessonEmployeesList)
                        }
                    }

                    lessonGroupEntityList?.let { lessonGroupList ->
                        withContext(Dispatchers.IO) {
                            scheduleLessonGroupDao.save(lessonGroupList)
                        }
                    }

                    scheduleLessonTemplateToEntityMapper.map(lesson, lessonMapperContext)
                }

                withContext(Dispatchers.IO) {
                    lessonEntities?.let { scheduleLessonDao.save(it) }
                }
            }
        }
    }

    override suspend fun deleteScheduleByEmployeeId(employeeId: Long): Resource<Unit> {
        return Resource.tryWithSuspend {
            withContext(Dispatchers.IO) { scheduleDao.deleteScheduleByEmployeeId(employeeId) }
        }
    }

    override suspend fun deleteScheduleByGroupId(groupId: Long): Resource<Unit> {
        return Resource.tryWithSuspend {
            withContext(Dispatchers.IO) { scheduleDao.deleteScheduleByGroupId(groupId) }
        }
    }

    override suspend fun clear(): Resource<Unit> {
        return Resource.tryWithSuspend {
            withContext(Dispatchers.IO) { scheduleDao.clear() }
        }
    }

}


