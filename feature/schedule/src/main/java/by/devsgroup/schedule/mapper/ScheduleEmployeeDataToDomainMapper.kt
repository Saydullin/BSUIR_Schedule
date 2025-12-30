package by.devsgroup.schedule.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.common.ScheduleEmployee
import by.devsgroup.schedule.server.model.ScheduleEmployeeData
import javax.inject.Inject

class ScheduleEmployeeDataToDomainMapper @Inject constructor(
): Mapper<ScheduleEmployeeData, ScheduleEmployee> {

    override fun map(from: ScheduleEmployeeData): ScheduleEmployee {
        return ScheduleEmployee(
            id = from.id,
            firstName = from.firstName,
            lastName = from.lastName,
            middleName = from.middleName,
            degree = from.degree,
            rank = from.rank,
            photoLink = from.photoLink,
            calendarId = from.calendarId,
            urlId = from.urlId,
            degreeAbbrev = from.degreeAbbrev,
            email = from.email,
            chief = from.chief,
        )
    }

}


