package com.bsuir.schedule.mapper

import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.schedule.common.ScheduleLessonEmployee
import com.bsuir.schedule.server.model.ScheduleLessonEmployeeData
import javax.inject.Inject

class ScheduleLessonEmployeeDataToDomainMapper @Inject constructor(
): Mapper<ScheduleLessonEmployeeData, ScheduleLessonEmployee> {

    override fun map(from: ScheduleLessonEmployeeData): ScheduleLessonEmployee {
        return ScheduleLessonEmployee(
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


