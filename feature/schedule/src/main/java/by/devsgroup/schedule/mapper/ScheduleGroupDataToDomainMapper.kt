package by.devsgroup.schedule.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.common.ScheduleGroup
import by.devsgroup.schedule.server.model.ScheduleGroupData
import javax.inject.Inject

class ScheduleGroupDataToDomainMapper @Inject constructor(
): Mapper<ScheduleGroupData, ScheduleGroup> {

    override fun map(from: ScheduleGroupData): ScheduleGroup {
        return ScheduleGroup(
            name = from.name,
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


