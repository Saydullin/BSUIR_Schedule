package by.devsgroup.schedule.mapper.entityToDomain

import by.devsgroup.database.schedule.relation.LessonsWithEmployeesAndGroups
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.common.ScheduleType
import by.devsgroup.domain.model.schedule.full.FullScheduleLesson
import java.time.DayOfWeek
import javax.inject.Inject

class ScheduleLessonEntityToDomainMapper @Inject constructor(
    private val scheduleGroupEntityToDomainMapper: ScheduleGroupEntityToDomainMapper,
    private val scheduleEmployeeEntityToDomainMapper: ScheduleEmployeeEntityToDomainMapper,
    private val scheduleLessonGroupEntityToDomainMapper: ScheduleLessonGroupEntityToDomainMapper,
    private val scheduleLessonEmployeeEntityToDomainMapper: ScheduleLessonEmployeeEntityToDomainMapper,
): Mapper<LessonsWithEmployeesAndGroups, FullScheduleLesson> {

    override fun map(from: LessonsWithEmployeesAndGroups): FullScheduleLesson {
        return FullScheduleLesson(
            audiences = from.lesson.audiences?.split(", "),
            dayOfWeek = from.lesson.dayOfWeek?.let { DayOfWeek.valueOf(it) },
            endLessonTime = from.lesson.endLessonTime,
            startLessonTime = from.lesson.startLessonTime,
            lessonTypeAbbrev = from.lesson.lessonTypeAbbrev,
            studentGroups = from.groups.map { scheduleLessonGroupEntityToDomainMapper.map(it) },
            subject = from.lesson.subject,
            subjectFullName = from.lesson.subjectFullName,
            weekNumber = from.lesson.weekNumber?.split(",")?.mapNotNull { it.trim().toIntOrNull() } ?: listOf(),
            employees = from.employees.map { scheduleLessonEmployeeEntityToDomainMapper.map(it) },
            dateLesson = from.lesson.dateLesson,
            startLessonDate = from.lesson.startLessonDate,
            endLessonDate = from.lesson.endLessonDate,
            announcement = from.lesson.announcement,
            split = from.lesson.split,
            scheduleType = ScheduleType.valueOf(from.lesson.scheduleType),
            scheduleGroup = from.scheduleGroup?.let { scheduleGroupEntityToDomainMapper.map(it) },
            scheduleEmployee = from.scheduleEmployee?.let { scheduleEmployeeEntityToDomainMapper.map(it) },
        )
    }

}