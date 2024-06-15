package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.EducationFormTable
import com.bsuir.bsuirschedule.data.db.entities.FacultyTable
import com.bsuir.bsuirschedule.data.db.entities.GroupTable
import com.bsuir.bsuirschedule.data.db.entities.SpecialityTable
import com.google.gson.annotations.SerializedName

data class GroupDTO (
    val id: Int?,
    var title: String?,
    val name: String?,
    var facultyId: Int?,
    var facultyAbbrev: String?,
    var facultyName: String?,
    @SerializedName("specialityDepartmentEducationFormId") var specialityId: Int?,
    var specialityName: String?,
    var specialityAbbrev: String?,
    val course: Int?,
    val calendarId: String?,
    val educationDegree: String?,
) {

    companion object {
        val empty = GroupDTO(
            id = -1,
            title = "",
            name = "",
            facultyId = -1,
            facultyAbbrev = "",
            facultyName = "",
            specialityId = -1,
            specialityName = "",
            specialityAbbrev = "",
            course = -1,
            calendarId = "",
            educationDegree = "",
        )
    }

    fun getFacultyAndSpecialityAbbr() = "${facultyAbbrev ?: ""} ${specialityAbbrev ?: ""}".trim()

    fun getFacultyAndSpecialityFull(): String {
        var facultyDescription = "${facultyAbbrev ?: ""} - ${facultyName?.lowercase() ?: ""}".trim()
        facultyDescription += "\n\n${specialityAbbrev ?: ""} - ${specialityName?.lowercase() ?: ""}"

        return facultyDescription
    }

    fun toGroup() = Group(
        id = id ?: -1,
        title = title,
        name = name ?: "",
        facultyId = facultyId ?: -1,
        faculty = Faculty(
            id = facultyId ?: -1,
            name = facultyName ?: "",
            abbrev = facultyAbbrev ?: ""
        ),
        specialityId = specialityId ?: -1,
        speciality = Speciality(
            id = specialityId ?: -1,
            name = specialityName ?: "",
            abbrev = specialityAbbrev ?: "",
            educationForm = EducationForm.empty,
            facultyId = facultyId ?: -1,
            code = ""
        ),
        course = course ?: -1,
        calendarId = "",
        isSaved = false,
        isSelected = false,
    )

    fun getTitleOrName(): String {
        return if (title.isNullOrEmpty()) {
            return "$name ($title)"
        } else {
            return name ?: ""
        }
    }

    fun toGroupTable() = GroupTable(
        id = id ?: -1,
        title = title ?: "",
        name = name ?: "",
        facultyId = facultyId ?: -1,
        specialityId = specialityId ?: -1,
        faculty = FacultyTable(
            id = facultyId ?: -1,
            name = facultyName ?: "",
            abbr = facultyAbbrev ?: "",
        ),
        speciality = SpecialityTable(
            id = specialityId ?: -1,
            name = specialityName ?: "",
            abbrev = specialityAbbrev ?: "",
            facultyId = facultyId ?: -1,
            code = "",
            educationForm = EducationFormTable(
                id = 0,
                name = ""
            ),
        ),
        course = course ?: -1,
        calendarId = calendarId ?: "",
        isSaved = false
    )

}


