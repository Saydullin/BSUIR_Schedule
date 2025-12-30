package by.devsgroup.schedule.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.template.ScheduleTemplate
import by.devsgroup.schedule.server.model.ScheduleData
import javax.inject.Inject

class ScheduleDataToDomainMapper @Inject constructor(
    private val scheduleLessonDataToDomainTemplateMapper: ScheduleLessonDataToDomainTemplateMapper,
    private val scheduleEmployeeDataToDomainMapper: ScheduleEmployeeDataToDomainMapper,
    private val scheduleGroupDataToDomainMapper: ScheduleGroupDataToDomainMapper,
): Mapper<ScheduleData, ScheduleTemplate> {

    override fun map(from: ScheduleData): ScheduleTemplate {
        val allSchedulesDays = listOf(
            from.schedules?.monday,
            from.schedules?.tuesday,
            from.schedules?.wednesday,
            from.schedules?.thursday,
            from.schedules?.friday,
            from.schedules?.saturday,
        )

        val allNextSchedulesDays = listOf(
            from.nextSchedules?.monday,
            from.nextSchedules?.tuesday,
            from.nextSchedules?.wednesday,
            from.nextSchedules?.thursday,
            from.nextSchedules?.friday,
            from.nextSchedules?.saturday,
        )

        val schedules = allSchedulesDays.mapNotNull { day ->
            day?.map { scheduleLessonDataToDomainTemplateMapper.map(it) }
        }.flatten()

        val nextSchedules = allNextSchedulesDays.mapNotNull { day ->
            day?.map { scheduleLessonDataToDomainTemplateMapper.map(it) }
        }.flatten()

        val exams = from.exams?.map {
            scheduleLessonDataToDomainTemplateMapper.map(it)
        }

        return ScheduleTemplate(
            startDate = from.startDate,
            endDate = from.endDate,
            startExamsDate = from.startExamsDate,
            endExamsDate = from.endExamsDate,
            employeeDto = from.employeeDto?.let { scheduleEmployeeDataToDomainMapper.map(it) },
            studentGroupDto = from.studentGroupDto?.let { scheduleGroupDataToDomainMapper.map(it) },
            schedules = schedules,
            nextSchedules = nextSchedules,
            currentTerm = from.currentTerm,
            nextTerm = from.nextTerm,
            exams = exams,
            currentPeriod = from.currentPeriod,
            partTimeOrRemote = from.isZaochOrDist,
        )
    }

}