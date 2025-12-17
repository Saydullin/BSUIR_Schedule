package by.devsgroup.groups.ui.mapper

import by.devsgroup.domain.mapper.MapperIndexed
import by.devsgroup.domain.model.groups.Group
import by.devsgroup.groups.ui.model.GroupUI
import javax.inject.Inject

class GroupToUiMapper @Inject constructor(
): MapperIndexed<Group, GroupUI> {

    override fun map(from: Group, index: Int): GroupUI {
        return GroupUI(
            uniqueListId = index,
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