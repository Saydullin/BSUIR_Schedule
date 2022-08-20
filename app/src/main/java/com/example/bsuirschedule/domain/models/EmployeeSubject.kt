package com.example.bsuirschedule.domain.models

import com.example.bsuirschedule.data.db.entities.EmployeeTable
import java.util.*
import kotlin.collections.ArrayList

data class EmployeeSubject(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val degree: String?,
    val degreeAbbrev: String?,
    val rank: String?,
    val photoLink: String,
    val calendarId: String?,
    val email: String?,
    val department: List<String>?,
    var departmentsList: ArrayList<Department>?,
    val urlId: String,
    val jobPosition: String?
) {

    companion object {
        val empty = EmployeeSubject(
            id = -1,
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
        id = id,
        employee = this.toEmployeeTable().toEmployee(),
        group = Group.empty,
        isGroup = false,
        lastUpdateTime = Date().time,
        isExistExams = false
    )

    fun toEmployeeTable() = EmployeeTable(
        id = id,
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        fullName = getFullName(),
        degree = degree ?: "",
        degreeAbbrev = degreeAbbrev ?: "",
        rank = rank ?: "",
        email = email ?: "",
        photoLink = photoLink,
        calendarId = calendarId ?: "",
        jobPosition = jobPosition ?: "",
        academicDepartment = department ?: listOf(),
        departments = departmentsList?.map { it.toDepartmentTable() } ?: listOf(),
        urlId = urlId,
    )

    fun toEmployee() = Employee(
        id = id,
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        degreeFull = degree,
        degreeAbbrev = degreeAbbrev,
        rank = rank,
        photoLink = photoLink,
        calendarId = calendarId,
        departmentsAbbrList = department,
        departments = departmentsList ?: arrayListOf(),
        urlId = urlId,
        isSaved = true
    )

    fun getShortDepartmentsAbbr(): String {
        if (departmentsList.isNullOrEmpty()) return ""

        return if (departmentsList!!.size == 1) {
            departmentsList!![0].abbrev
        } else {
            departmentsList!![0].abbrev + ", +" + (departmentsList!!.size - 1)
        }
    }

    fun getRankAndDegree() = "${degreeAbbrev ?: ""} ${rank ?: ""}".trim()

    fun getFullName() = "$lastName $firstName $middleName"

    fun getName() = "$lastName ${firstName[0]}. ${middleName[0]}.".trim()

}


