package by.devsgroup.schedule.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.Schedule
import by.devsgroup.schedule.server.model.ScheduleData
import javax.inject.Inject

class ScheduleDataToDomainMapper @Inject constructor(
    private val scheduleLessonEmployeeDataToDomainMapper: ScheduleLessonEmployeeDataToDomainMapper
): Mapper<ScheduleData, Schedule> {

    override fun map(from: ScheduleData): Schedule {
        return Schedule(
            startDate = from.startDate,
            endDate = from.endDate,
            startExamsDate = from.startExamsDate,
            endExamsDate = from.endExamsDate,
            employeeDto = from.employeeDto?.let { scheduleLessonEmployeeDataToDomainMapper.map(it) },
            studentGroupDto = from.studentGroupDto,
            schedules = listOf(), // TODO
            nextSchedules = listOf(), // TODO
            currentTerm = from.currentTerm,
            nextTerm = from.nextTerm,
            exams = listOf(), // TODO
            currentPeriod = from.currentPeriod,
            partTimeOrRemote = from.isZaochOrDist,
        )
    }

}