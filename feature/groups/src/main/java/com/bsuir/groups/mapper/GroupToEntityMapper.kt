package com.bsuir.groups.mapper

import com.bsuir.database.groups.entity.GroupEntity
import com.bsuir.domain.mapper.MapperNullable
import com.bsuir.domain.model.group.Group
import javax.inject.Inject

class GroupToEntityMapper @Inject constructor(
): MapperNullable<Group, GroupEntity> {

    override fun map(from: Group): GroupEntity? {
        val name = from.name

        return if (name.isNullOrEmpty()) {
            null
        } else {
            GroupEntity(
                id = from.id,
                name = name,
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

}


