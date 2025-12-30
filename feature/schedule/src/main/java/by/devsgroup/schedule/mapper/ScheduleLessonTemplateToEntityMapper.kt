package by.devsgroup.schedule.mapper

import by.devsgroup.database.schedule.entity.ScheduleLessonEntity
import by.devsgroup.domain.mapper.MapperWithContext
import by.devsgroup.domain.model.schedule.template.ScheduleLessonTemplate
import by.devsgroup.schedule.mapper.context.ScheduleLessonContext
import javax.inject.Inject

class ScheduleLessonTemplateToEntityMapper @Inject constructor(
): MapperWithContext<ScheduleLessonTemplate, ScheduleLessonEntity, ScheduleLessonContext> {

    override fun map(from: ScheduleLessonTemplate, context: ScheduleLessonContext): ScheduleLessonEntity {
        return ScheduleLessonEntity(
            scheduleId = context.scheduleId,
            lessonId = context.lessonId,
            audiences = from.audiences?.joinToString(", "),
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


