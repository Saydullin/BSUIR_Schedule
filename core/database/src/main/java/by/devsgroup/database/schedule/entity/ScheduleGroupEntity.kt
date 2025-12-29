package by.devsgroup.database.schedule.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedule_group"
)
data class ScheduleGroupEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val name: String?,
    val facultyId: Int?,
    val facultyAbbrev: String?,
    val facultyName: String?,
    val specialityDepartmentEducationFormId: Int?,
    val specialityName: String?,
    val specialityAbbrev: String?,
    val course: Int?,
    val id: Int?,
    val calendarId: String?,
    val educationDegree: Int?,
)


