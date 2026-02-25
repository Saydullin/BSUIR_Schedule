package com.bsuir.schedule.mapper

import com.bsuir.database.schedule.entity.ScheduleEmployeeEntity
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.schedule.common.ScheduleEmployee
import javax.inject.Inject

class ScheduleEmployeeToDomainMapper @Inject constructor(
): Mapper<ScheduleEmployeeEntity, ScheduleEmployee> {

    override fun map(from: ScheduleEmployeeEntity): ScheduleEmployee {
        return ScheduleEmployee(
            id = from.id,
            firstName = from.firstName,
            lastName = from.lastName,
            middleName = from.middleName,
            photoLink = from.photoLink,
            degree = from.degree,
            degreeAbbrev = from.degreeAbbrev,
            rank = from.rank,
            email = from.email,
            urlId = from.urlId,
            calendarId = from.calendarId,
            chief = from.chief,
        )
    }

}