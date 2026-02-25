package com.bsuir.schedule.mapper.entityToDomain

import com.bsuir.database.schedule.relation.ScheduleWithDays
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.schedule.full.FullSchedule
import javax.inject.Inject

class ScheduleWithDaysEntityToDomainMapper @Inject constructor(
    private val scheduleEmployeeEntityToDomainMapper: ScheduleEmployeeEntityToDomainMapper,
    private val daysWithLessonsEntityToDomainMapper: DaysWithLessonsEntityToDomainMapper,
    private val scheduleGroupEntityToDomainMapper: ScheduleGroupEntityToDomainMapper,
): Mapper<ScheduleWithDays, FullSchedule> {

    override fun map(from: ScheduleWithDays): FullSchedule {
        return FullSchedule(
            startDate = from.schedule.startDate,
            endDate = from.schedule.endDate,
            startExamsDate = from.schedule.startExamsDate,
            endExamsDate = from.schedule.endExamsDate,
            employee = from.schedule.employee?.let { scheduleEmployeeEntityToDomainMapper.map(it) },
            group = from.schedule.group?.let { scheduleGroupEntityToDomainMapper.map(it) },
            schedules = from.days.map { daysWithLessonsEntityToDomainMapper.map(it) },
            nextSchedules = listOf(), // TODO
            currentTerm = from.schedule.currentTerm,
            nextTerm = from.schedule.nextTerm,
            exams = listOf(), // TODO
            currentPeriod = from.schedule.currentPeriod,
            partTimeOrRemote = from.schedule.partTimeOrRemote,
        )
    }

}


