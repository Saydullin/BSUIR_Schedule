package by.devsgroup.schedule.mapper

import by.devsgroup.database.schedule.entity.ScheduleEmployeeEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.mapper.MapperWithContext
import by.devsgroup.domain.model.schedule.common.ScheduleEmployee
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