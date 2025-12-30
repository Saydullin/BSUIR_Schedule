package by.devsgroup.schedule.mapper.entityToDomain

import by.devsgroup.database.schedule.entity.ScheduleEmployeeEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.common.ScheduleEmployee
import javax.inject.Inject

class ScheduleEmployeeEntityToDomainMapper @Inject constructor(
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