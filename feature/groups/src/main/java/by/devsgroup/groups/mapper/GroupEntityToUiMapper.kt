package by.devsgroup.groups.mapper

import by.devsgroup.database.groups.entity.GroupEntity
import by.devsgroup.domain.mapper.Mapper
import by.devsgroup.groups.ui.model.GroupUI
import javax.inject.Inject

class GroupEntityToUiMapper @Inject constructor(
): Mapper<GroupEntity, GroupUI> {

    override fun map(from: GroupEntity): GroupUI {
        return GroupUI(
            uniqueListId = from.tableId,
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