package com.bsuir.groups.mapper

import com.bsuir.domain.mapper.Mapper
import com.bsuir.domain.model.group.Group
import com.bsuir.groups.server.model.GroupData
import javax.inject.Inject

class GroupDataToDomainMapper @Inject constructor(
): Mapper<GroupData, Group> {

    override fun map(from: GroupData): Group {
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


