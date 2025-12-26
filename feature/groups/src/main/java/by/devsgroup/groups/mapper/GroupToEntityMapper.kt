package by.devsgroup.groups.mapper

import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.model.groups.Group
import by.devsgroup.groups.data.db.entity.GroupEntity
import javax.inject.Inject

class GroupToEntityMapper @Inject constructor(
): Mapper<Group, GroupEntity> {

    override fun map(from: Group): GroupEntity {
        return GroupEntity(
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


