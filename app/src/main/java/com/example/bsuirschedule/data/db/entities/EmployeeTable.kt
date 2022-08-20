package com.example.bsuirschedule.data.db.entities

import androidx.room.*
import com.example.bsuirschedule.domain.models.Department
import com.example.bsuirschedule.domain.models.Employee
import com.example.bsuirschedule.domain.models.EmployeeSubject

@Entity
data class EmployeeTable (
    @PrimaryKey val id: Int,
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
    @ColumnInfo val urlId: String,
) {

    companion object {
        val empty = EmployeeTable(
            -1,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            listOf(),
            listOf(),
            ""
        )
    }

    fun toEmployeeSubject() = EmployeeSubject(
        id = id,
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
        isSaved = false,
        urlId = urlId,
    )

}


