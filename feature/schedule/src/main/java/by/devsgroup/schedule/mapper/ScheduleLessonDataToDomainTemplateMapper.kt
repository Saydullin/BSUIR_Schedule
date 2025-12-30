package by.devsgroup.schedule.mapper

import by.devsgroup.domain.mapper.MapperWithContext
import by.devsgroup.domain.model.schedule.template.ScheduleLessonTemplate
import by.devsgroup.schedule.server.model.ScheduleLessonData
import java.time.DayOfWeek
import javax.inject.Inject

class ScheduleLessonDataToDomainTemplateMapper @Inject constructor(
    private val scheduleLessonGroupDataToDomainTemplate: ScheduleLessonGroupDataToDomainTemplate,
    private val scheduleLessonEmployeeDataToDomainMapper: ScheduleLessonEmployeeDataToDomainMapper,
): MapperWithContext<ScheduleLessonData, ScheduleLessonTemplate, DayOfWeek?> {

    override fun map(from: ScheduleLessonData, context: DayOfWeek?): ScheduleLessonTemplate {
        return ScheduleLessonTemplate(
            dayOfWeek = context,
            audiences = from.audiences,
            endLessonTime = from.endLessonTime,
            startLessonTime = from.startLessonTime,
            lessonTypeAbbrev = from.lessonTypeAbbrev,
            studentGroups = from.studentGroups?.map { scheduleLessonGroupDataToDomainTemplate.map(it) },
            subject = from.subject,
            subjectFullName = from.subjectFullName,
            weekNumber = from.weekNumber,
            employees = from.employees?.map { scheduleLessonEmployeeDataToDomainMapper.map(it) },
            dateLesson = from.dateLesson,
            startLessonDate = from.startLessonDate,
            endLessonDate = from.endLessonDate,
            announcement = from.announcement,
            split = from.split,
        )
    }

}


