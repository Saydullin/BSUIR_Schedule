package com.bsuir.schedule.mapper

import com.bsuir.database.schedule.entity.ScheduleEmployeeEntity
import com.bsuir.domain.mapper.MapperWithContext
import com.bsuir.domain.model.schedule.common.ScheduleEmployee
import javax.inject.Inject

class ScheduleEmployeeToEntityMapper @Inject constructor(
): MapperWithContext<ScheduleEmployee, ScheduleEmployeeEntity, Long> {

    override fun map(from: ScheduleEmployee, context: Long): ScheduleEmployeeEntity {
        return ScheduleEmployeeEntity(
            firstName = from.firstName,
            lastName = from.lastName,
            middleName = from.middleName,
            degree = from.degree,
            degreeAbbrev = from.degreeAbbrev,
            email = from.email,
            rank = from.rank,
            photoLink = from.photoLink,
            calendarId = from.calendarId,
            chief = from.chief,
            id = from.id,
            urlId = from.urlId,
            scheduleId = context,
        )
    }

}