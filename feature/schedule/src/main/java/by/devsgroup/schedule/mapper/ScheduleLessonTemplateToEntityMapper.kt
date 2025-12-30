package by.devsgroup.schedule.mapper

import by.devsgroup.database.schedule.entity.ScheduleLessonEntity
import by.devsgroup.domain.mapper.MapperWithContext
import by.devsgroup.domain.model.schedule.full.FullScheduleLesson
import by.devsgroup.schedule.mapper.context.ScheduleLessonContext
import javax.inject.Inject

class ScheduleLessonTemplateToEntityMapper @Inject constructor(
): MapperWithContext<FullScheduleLesson, ScheduleLessonEntity, ScheduleLessonContext> {

    override fun map(from: FullScheduleLesson, context: ScheduleLessonContext): ScheduleLessonEntity {
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
        )
    }

}


