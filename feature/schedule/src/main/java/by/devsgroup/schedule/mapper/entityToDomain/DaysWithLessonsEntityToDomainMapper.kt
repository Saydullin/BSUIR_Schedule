package by.devsgroup.schedule.mapper.entityToDomain

import by.devsgroup.database.schedule.relation.DaysWithLessons
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.full.FullScheduleDay
import javax.inject.Inject

class DaysWithLessonsEntityToDomainMapper @Inject constructor(
    private val scheduleLessonEntityToDomainMapper: ScheduleLessonEntityToDomainMapper
): Mapper<DaysWithLessons, FullScheduleDay> {

    override fun map(from: DaysWithLessons): FullScheduleDay {
        return FullScheduleDay(
            lessons = from.lessons.map { scheduleLessonEntityToDomainMapper.map(it) },
            date = from.day.dateMillis,
            week = from.day.week,
        )
    }

}