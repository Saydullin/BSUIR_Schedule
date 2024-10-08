package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.EmployeeTable
import kotlin.collections.ArrayList

data class EmployeeSubject(
    val id: Int?,
    var title: String?,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val degree: String?,
    val degreeAbbrev: String?,
    val rank: String?,
    val photoLink: String?,
    val calendarId: String?,
    val email: String?,
    val department: List<String>?,
    var departmentsList: ArrayList<Department>?,
    val urlId: String?,
    val jobPosition: String?
) {

    companion object {
        val empty = EmployeeSubject(
            id = -1,
            title = "",
            firstName = "",
            lastName = "",
            middleName = "",
            degree = "",
            degreeAbbrev = "",
            rank = "",
            photoLink = "",
            calendarId = "",
            email = "",
            department = listOf(),
            departmentsList = arrayListOf(),
            urlId = "",
            jobPosition = ""
        )
    }

    fun toSavedSchedule() = SavedSchedule(
        id = id ?: -1,
        employee = this.toEmployeeTable().toEmployee(),
        group = Group.empty,
        isGroup = false,
        lastUpdateTime = 0,
        lastUpdateDate = 0,
        isUpdatedSuccessfully = false,
        isExistExams = false
    )

    fun toEmployeeTable() = EmployeeTable(
        id = id ?: -1,
        title = title ?: "",
        firstName = firstName ?: "",
        lastName = lastName ?: "",
        middleName = middleName ?: "",
        fullName = getFullName(),
        degree = degree ?: "",
        degreeAbbrev = degreeAbbrev ?: "",
        rank = rank ?: "",
        email = email ?: "",
        photoLink = photoLink ?: "",
        calendarId = calendarId ?: "",
        jobPosition = jobPosition ?: "",
        academicDepartment = department ?: listOf(),
        departments = departmentsList?.map { it.toDepartmentTable() } ?: listOf(),
        isSaved = null,
        urlId = urlId ?: "",
    )

    fun toEmployee() = Employee(
        id = id ?: -1,
        title = title ?: "",
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        degreeFull = degree,
        degreeAbbrev = degreeAbbrev,
        rank = rank,
        photoLink = photoLink,
        calendarId = calendarId,
        email = email,
        departmentsAbbrList = department,
        departments = departmentsList ?: arrayListOf(),
        urlId = urlId ?: "",
        isSaved = false
    )

    fun getFullDepartments(separator: String): String {
        return departmentsList?.joinToString(separator) { dep ->
            "${dep.abbrev.replaceFirstChar { it.lowercase() }} - ${dep.name.lowercase()}"
        } ?: ""
    }

    fun getShortDepartments(separator: String): String {
        if (departmentsList.isNullOrEmpty()) return ""
        return departmentsList!![0].abbrev.replaceFirstChar { it.lowercase() } +
                if (departmentsList!!.size > 1) {
            ", $separator ${departmentsList!!.size - 1}"
        } else ""
    }

    fun getRankAndDegree() = "${degreeAbbrev ?: ""} ${rank ?: ""}".trim()

    fun getTitleOrFullName(): String {
        if (title.isNullOrEmpty()) {
            return getFullName()
        }

        return "${getName()} ($title)"
    }

    fun getFullName() = "$lastName $firstName $middleName"

    fun getName(): String {
        val firstNameLetter = firstName?.getOrNull(0)?.let { "$it." } ?: ""
        val middleNameLetter = middleName?.getOrNull(0)?.let { "$it." } ?: ""

        return "$lastName $firstNameLetter $middleNameLetter".trim()
    }

}


