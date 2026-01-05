package by.devsgroup.groups.mapper

import by.devsgroup.database.groups.entity.GroupEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.domain.mapper.MapperNullable
import by.devsgroup.domain.model.group.Group
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


