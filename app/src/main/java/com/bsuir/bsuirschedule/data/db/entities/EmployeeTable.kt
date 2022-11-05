package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.*
import com.bsuir.bsuirschedule.domain.models.Department
import com.bsuir.bsuirschedule.domain.models.Employee
import com.bsuir.bsuirschedule.domain.models.EmployeeSubject

@Entity
data class EmployeeTable (
    @PrimaryKey val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val firstName: String,
    @ColumnInfo val lastName: String,
    @ColumnInfo val middleName: String,
    @ColumnInfo val fullName: String,
    @ColumnInfo val degree: String,
    @ColumnInfo val degreeAbbrev: String,
    @ColumnInfo val rank: String,
    @ColumnInfo val email: String,
    @ColumnInfo val photoLink: String,
    @ColumnInfo val calendarId: String,
    @ColumnInfo val jobPosition: String,
    @ColumnInfo val academicDepartment: List<String>,
    @ColumnInfo val departments: List<DepartmentTable>,
    @ColumnInfo val isSaved: Boolean?,
    @ColumnInfo val urlId: String,
) {

    companion object {
        val empty = EmployeeTable(
            id = -1,
            title = "",
            firstName = "",
            lastName = "",
            middleName = "",
            fullName = "",
            degree = "",
            degreeAbbrev = "",
            rank = "",
            email= "",
            photoLink = "",
            calendarId = "",
            jobPosition = "",
            academicDepartment = listOf(),
            departments = listOf(),
            isSaved = false,
            urlId = ""
        )
    }

    fun toEmployeeSubject() = EmployeeSubject(
        id = id,
        title = title,
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        degree = degree,
        degreeAbbrev = degreeAbbrev,
        rank = rank,
        photoLink = photoLink,
        calendarId = calendarId,
        email = email,
        department = academicDepartment,
        departmentsList = departments.map { it.toDepartment() } as ArrayList<Department>,
        urlId = urlId,
        jobPosition = jobPosition
    )

    fun toEmployee() = Employee(
        id = id,
        title = title,
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        degreeFull = degree,
        degreeAbbrev = degreeAbbrev,
        rank = rank,
        photoLink = photoLink,
        calendarId = calendarId,
        departmentsAbbrList = academicDepartment,
        departments = departments.map { it.toDepartment() } as ArrayList<Department>,
        isSaved = isSaved ?: false,
        urlId = urlId,
    )

}


