package com.bsuir.schedule.mapper

import com.bsuir.database.schedule.entity.ScheduleLessonEntity
import com.bsuir.domain.mapper.MapperWithContext
import com.bsuir.domain.model.schedule.full.FullScheduleLesson
import com.bsuir.schedule.mapper.context.ScheduleLessonToEntityMapperContext
import javax.inject.Inject

class ScheduleLessonTemplateToEntityMapper @Inject constructor(
): MapperWithContext<FullScheduleLesson, ScheduleLessonEntity, ScheduleLessonToEntityMapperContext> {

    override fun map(from: FullScheduleLesson, context: ScheduleLessonToEntityMapperContext): ScheduleLessonEntity {
        return ScheduleLessonEntity(
            scheduleId = context.scheduleId,
            lessonId = context.lessonId,
            dayOfWeek = from.dayOfWeek?.toString(),
            dayId = context.dayId,
            audiences = from.audiences?.joinToString(", "),
            weekNumber = from.weekNumber.joinToString(", "),
            endLessonTime = from.endLessonTime,
            startLessonTime = from.startLessonTime,
            lessonTypeAbbrev = from.lessonTypeAbbrev,
            subject = from.subject,
            subjectFullName = from.subjectFullName,
            dateLesson = from.dateLesson,
            startLessonDate = from.startLessonDate,
            endLessonDate = from.endLessonDate,
            announcement = from.announcement,
            split = from.split,
            scheduleType = from.scheduleType.toString(),
        )
    }

}


