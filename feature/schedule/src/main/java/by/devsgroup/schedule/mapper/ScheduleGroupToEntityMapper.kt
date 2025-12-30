package by.devsgroup.schedule.mapper

import by.devsgroup.database.schedule.entity.ScheduleGroupEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.schedule.common.ScheduleGroup
import javax.inject.Inject

class ScheduleGroupToEntityMapper @Inject constructor(
): Mapper<ScheduleGroup, ScheduleGroupEntity> {

    override fun map(from: ScheduleGroup): ScheduleGroupEntity {
        return ScheduleGroupEntity(
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