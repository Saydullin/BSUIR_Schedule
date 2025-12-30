package by.devsgroup.schedule.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.template.ScheduleLessonTemplate
import by.devsgroup.schedule.server.model.ScheduleLessonData
import javax.inject.Inject

class ScheduleLessonDataToDomainTemplateMapper @Inject constructor(
    private val scheduleLessonGroupDataToDomainTemplate: ScheduleLessonGroupDataToDomainTemplate,
    private val scheduleLessonEmployeeDataToDomainMapper: ScheduleLessonEmployeeDataToDomainMapper,
): Mapper<ScheduleLessonData, ScheduleLessonTemplate> {

    override fun map(from: ScheduleLessonData): ScheduleLessonTemplate {
        return ScheduleLessonTemplate(
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


