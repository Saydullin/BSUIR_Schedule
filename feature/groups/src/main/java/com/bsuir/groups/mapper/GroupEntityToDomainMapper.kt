package com.bsuir.groups.mapper

import com.bsuir.database.groups.entity.GroupEntity
import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.group.Group
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


