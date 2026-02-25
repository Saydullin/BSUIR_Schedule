package com.bsuir.schedule.mapper

import com.bsuir.database.schedule.entity.ScheduleGroupEntity
import com.bsuir.domain.mapper.MapperWithContext
import com.bsuir.domain.model.schedule.common.ScheduleGroup
import javax.inject.Inject

class ScheduleGroupToEntityMapper @Inject constructor(
): MapperWithContext<ScheduleGroup, ScheduleGroupEntity, Long> {

    override fun map(from: ScheduleGroup, context: Long): ScheduleGroupEntity {
        return ScheduleGroupEntity(
            name = from.name,
            scheduleId = context,
            facultyId = from.facultyId,
            facultyAbbrev = from.facultyAbbrev,
            facultyName = from.facultyName,
            specialityDepartmentEducationFormId = from.specialityDepartmentEducationFormId,
            specialityName = from.specialityName,
            specialityAbbrev = from.specialityAbbrev,
            course = from.course,
            id = from.id,
            calendarId = from.calendarId,
            educationDegree = from.educationDegree,
        )
    }

}


