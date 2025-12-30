package by.devsgroup.schedule.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.full.FullScheduleLesson
import by.devsgroup.domain.model.schedule.template.ScheduleLessonTemplate
import javax.inject.Inject

class ScheduleLessonTemplateToFullMapper @Inject constructor(
): Mapper<ScheduleLessonTemplate, FullScheduleLesson> {

    override fun map(from: ScheduleLessonTemplate): FullScheduleLesson {
        return FullScheduleLesson(
            audiences = from.audiences,
            dayOfWeek = from.dayOfWeek,
            endLessonTime = from.endLessonTime,
            startLessonTime = from.startLessonTime,
            lessonTypeAbbrev = from.lessonTypeAbbrev,
            studentGroups = from.studentGroups,
            subject = from.subject,
            subjectFullName = from.subjectFullName,
            weekNumber = from.weekNumber ?: listOf(),
            employees = from.employees,
            dateLesson = from.dateLesson,
            startLessonDate = from.startLessonDate,
            endLessonDate = from.endLessonDate,
            announcement = from.announcement,
            split = from.split,
        )
    }

}