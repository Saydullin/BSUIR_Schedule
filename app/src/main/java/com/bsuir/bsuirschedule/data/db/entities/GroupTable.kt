package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.GroupDTO

@Entity
data class GroupTable (
    @PrimaryKey val id: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val name: String,
    @ColumnInfo val facultyId: Int,
    @Embedded(prefix = "faculty_") val faculty: FacultyTable,
    @Embedded(prefix = "speciality_") val speciality: SpecialityTable,
    @ColumnInfo val specialityId: Int,
    @ColumnInfo val course: Int,
    @ColumnInfo val calendarId: String,
    @ColumnInfo val isSaved: Boolean
) {

    fun toGroup() = Group(
        id = id,
        title = title,
        name = name,
        facultyId = facultyId,
        faculty = faculty.toFaculty(),
        specialityId = specialityId,
        speciality = speciality.toSpeciality(),
        course = course,
        calendarId = calendarId,
        isSaved = isSaved
    )

    fun toGroupDTO() = GroupDTO(
        id = id,
        title = title,
        name = name,
        facultyId = facultyId,
        facultyAbbrev = faculty.abbr,
        facultyName = faculty.name,
        specialityId = specialityId,
        specialityName = speciality.name,
        specialityAbbrev = speciality.abbrev,
        course = course,
        calendarId = calendarId,
        educationDegree = "",
    )

}


