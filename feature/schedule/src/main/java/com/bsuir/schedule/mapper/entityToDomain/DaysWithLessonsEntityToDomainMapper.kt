package com.bsuir.schedule.mapper.entityToDomain

import com.bsuir.database.schedule.relation.DaysWithLessons
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.schedule.full.FullScheduleDay
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