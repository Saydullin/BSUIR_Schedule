package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.EmployeeTable
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class Employee (
    val id: Int,
    var title: String?,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    var degreeFull: String?,
    @SerializedName("degree") val degreeAbbrev: String?,
    val rank: String?,
    val photoLink: String?,
    val calendarId: String?,
    val email: String?,
    @SerializedName("academicDepartment")
    val departmentsAbbrList: List<String>?,
    var departments: ArrayList<Department> = arrayListOf(),
    val urlId: String,
    var isSaved: Boolean
) {

    companion object {
        val empty = Employee(
            id = -1,
            title = "",
            firstName = "",
            lastName = "",
            middleName = "",
            degreeFull = "",
            degreeAbbrev = "",
            rank = "",
            photoLink = "",
            calendarId = "",
            email = "",
            listOf(),
            arrayListOf(),
            urlId = "",
            isSaved = false
        )
    }

    fun toEmployeeSubject() = EmployeeSubject(
        id = id,
        title = title,
        firstName = firstName ?: "",
        lastName = lastName ?: "",
        middleName = middleName ?: "",
        degree = degreeFull,
        degreeAbbrev = degreeAbbrev,
        rank = rank,
        photoLink = photoLink ?: "",
        calendarId = calendarId,
        email = email,
        department = departmentsAbbrList,
        departmentsList = departments,
        urlId = urlId,
        jobPosition = null
    )

    fun getEmployeeTitle(): String {
        return title ?: ""
    }

    fun getFullName() = "$lastName $firstName $middleName"

    fun getTitleOrFullName(): String {
        if (title.isNullOrEmpty()) {
            return getFullName()
        }

        return "${getName()} ($title)"
    }

    fun getName() = "$lastName ${firstName?.get(0)}. ${middleName?.get(0)}.".trim()

    fun getShortDepartmentsAbbr(): String {
        if (departmentsAbbrList.isNullOrEmpty()) return ""

        return if (departmentsAbbrList.size == 1) {
            departmentsAbbrList[0].replaceFirstChar { it.lowercase() }
        } else {
            departmentsAbbrList[0].replaceFirstChar { it.lowercase() } + ", +" + (departmentsAbbrList.size - 1)
        }
    }

    fun getShortDepartmentsAbbrList(separator: String): String {
        if (departmentsAbbrList.isNullOrEmpty()) return ""

        return departmentsAbbrList.joinToString(separator)
    }

    fun getDegreeAndRank() = "${degreeAbbrev ?: ""} ${rank ?: ""}".trim()

    fun toSavedSchedule(isExams: Boolean = false) = SavedSchedule(
        id = id,
        group = Group.empty,
        employee = this,
        isGroup = false,
        lastUpdateTime = 0,
        lastUpdateDate = 0,
        isUpdatedSuccessfully = false,
        isExistExams = isExams
    )

    fun toEmployeeTable() = EmployeeTable(
        id = id,
        title = title ?: "",
        firstName = firstName ?: "",
        lastName = lastName ?: "",
        middleName = middleName ?: "",
        fullName = getFullName(),
        degree = degreeFull ?: "",
        degreeAbbrev = degreeAbbrev ?: "",
        rank = rank ?: "",
        photoLink = photoLink ?: "",
        email = email ?: "",
        jobPosition = "",
        calendarId = calendarId ?: "",
        academicDepartment = departmentsAbbrList ?: listOf(),
        departments = departments.map { it.toDepartmentTable() },
        isSaved = isSaved,
        urlId = urlId
    )

}


