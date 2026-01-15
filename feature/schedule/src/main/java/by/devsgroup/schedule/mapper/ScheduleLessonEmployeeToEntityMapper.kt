package by.devsgroup.schedule.mapper

import by.devsgroup.database.schedule.entity.ScheduleLessonEmployeeEntity
import by.devsgroup.domain.mapper.MapperWithContext
import by.devsgroup.domain.model.schedule.common.ScheduleLessonEmployee
import javax.inject.Inject

class ScheduleLessonEmployeeToEntityMapper @Inject constructor(
): MapperWithContext<ScheduleLessonEmployee, ScheduleLessonEmployeeEntity, String> {

    override fun map(from: ScheduleLessonEmployee, context: String): ScheduleLessonEmployeeEntity {
        return ScheduleLessonEmployeeEntity(
            lessonId = context,
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


