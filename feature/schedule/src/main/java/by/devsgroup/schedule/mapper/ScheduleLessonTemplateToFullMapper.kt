package by.devsgroup.schedule.mapper

import by.devsgroup.domain.mapper.MapperWithContext
import by.devsgroup.domain.model.schedule.full.FullScheduleLesson
import by.devsgroup.domain.model.schedule.template.ScheduleLessonTemplate
import by.devsgroup.schedule.mapper.context.ScheduleLessonToFullMapperContext
import javax.inject.Inject

class ScheduleLessonTemplateToFullMapper @Inject constructor(
): MapperWithContext<ScheduleLessonTemplate, FullScheduleLesson, ScheduleLessonToFullMapperContext> {

    override fun map(from: ScheduleLessonTemplate, context: ScheduleLessonToFullMapperContext): FullScheduleLesson {
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
            scheduleType = context.scheduleType,
            scheduleGroup = context.group,
            scheduleEmployee = context.employee
        )
    }

}