package by.devsgroup.schedule.mapper.entityToDomain

import by.devsgroup.database.schedule.entity.ScheduleLessonEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.full.FullScheduleLesson
import java.time.DayOfWeek
import javax.inject.Inject

class ScheduleLessonEntityToDomainMapper @Inject constructor(
): Mapper<ScheduleLessonEntity, FullScheduleLesson> {

    override fun map(from: ScheduleLessonEntity): FullScheduleLesson {
        return FullScheduleLesson(
            audiences = from.audiences?.split(", "),
            dayOfWeek = from.dayOfWeek?.let { DayOfWeek.valueOf(it) },
            endLessonTime = from.endLessonTime,
            startLessonTime = from.startLessonTime,
            lessonTypeAbbrev = from.lessonTypeAbbrev,
            studentGroups = listOf(), // TODO
            subject = from.subject,
            subjectFullName = from.subjectFullName,
            weekNumber = from.weekNumber?.split(",")?.mapNotNull { it.trim().toIntOrNull() } ?: listOf(),
            employees = listOf(),
            dateLesson = from.dateLesson,
            startLessonDate = from.startLessonDate,
            endLessonDate = from.endLessonDate,
            announcement = from.announcement,
            split = from.split,
        )
    }

}