package com.example.bsuirschedule.domain.models

import com.example.bsuirschedule.data.db.entities.GroupTable
import com.google.gson.annotations.SerializedName
import java.util.*

data class Group (
    val id: Int,
    val name: String,
    val facultyId: Int,
    var faculty: Faculty?,
    @SerializedName("specialityDepartmentEducationFormId") val specialityId: Int,
    var speciality: Speciality?,
    val course: Int,
    val calendarId: String?,
    val isSaved: Boolean,
    var isSelected: Boolean = false
) {

    companion object {
        val empty = Group(
            id = -1,
            name = "",
            facultyId = -1,
            faculty = Faculty.empty,
            specialityId = -1,
            speciality = Speciality.empty,
            course = -1,
            calendarId = "",
            isSaved = false
        )
    }

    fun getFacultyAndSpecialityAbbr() = "${faculty?.abbrev ?: ""} ${speciality?.abbrev ?: ""}".trim()

    fun toSavedSchedule(isExams: Boolean) = SavedSchedule(
        id = id,
        group = this,
        employee = Employee.empty,
        isGroup = true,
        lastUpdateTime = Date().time,
        isExistExams = isExams
    )

    fun toGroupTable() = GroupTable(
        id = id,
        name = name,
        facultyId = facultyId,
        specialityId = specialityId,
        faculty = faculty?.toFacultyTable() ?: Faculty.empty.toFacultyTable(),
        speciality = speciality?.toSpecialityTable() ?: Speciality.empty.toSpecialityTable(),
        course = course,
        calendarId = calendarId ?: "",
        isSaved = isSaved
    )

}


