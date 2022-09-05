package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsuir.bsuirschedule.domain.models.Group

@Entity
data class GroupTable (
    @PrimaryKey val id: Int,
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
        name = name,
        facultyId = facultyId,
        faculty = faculty.toFaculty(),
        specialityId = specialityId,
        speciality = speciality.toSpeciality(),
        course = course,
        calendarId = calendarId,
        isSaved = isSaved
    )

}


