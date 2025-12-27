package by.devsgroup.groups.mapper

import by.devsgroup.database.groups.entity.GroupEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.groups.Group
import javax.inject.Inject

class GroupEntityToDomainMapper @Inject constructor(
): Mapper<GroupEntity, Group> {

    override fun map(from: GroupEntity): Group {
        return Group(
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


