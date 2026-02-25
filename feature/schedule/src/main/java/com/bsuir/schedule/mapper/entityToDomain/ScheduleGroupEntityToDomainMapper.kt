package com.bsuir.schedule.mapper.entityToDomain

import com.bsuir.database.schedule.entity.ScheduleGroupEntity
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.schedule.common.ScheduleGroup
import javax.inject.Inject

class ScheduleGroupEntityToDomainMapper @Inject constructor(
): Mapper<ScheduleGroupEntity, ScheduleGroup> {

    override fun map(from: ScheduleGroupEntity): ScheduleGroup {
        return ScheduleGroup(
            id = from.id,
            name = from.name,
            facultyId = from.facultyId,
            facultyAbbrev = from.facultyAbbrev,
            facultyName = from.facultyName,
            specialityDepartmentEducationFormId = from.specialityDepartmentEducationFormId,
            specialityName = from.specialityName,
            specialityAbbrev = from.specialityAbbrev,
            course = from.course,
            calendarId = from.calendarId,
            educationDegree = from.educationDegree,
        )
    }

}