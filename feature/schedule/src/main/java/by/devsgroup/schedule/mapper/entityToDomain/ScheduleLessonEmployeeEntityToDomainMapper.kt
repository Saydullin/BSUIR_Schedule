package by.devsgroup.schedule.mapper.entityToDomain

import by.devsgroup.database.schedule.entity.ScheduleLessonEmployeeEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.common.ScheduleLessonEmployee
import javax.inject.Inject

class ScheduleLessonEmployeeEntityToDomainMapper @Inject constructor(
): Mapper<ScheduleLessonEmployeeEntity, ScheduleLessonEmployee> {

    override fun map(from: ScheduleLessonEmployeeEntity): ScheduleLessonEmployee {
        return ScheduleLessonEmployee(
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