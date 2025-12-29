package by.devsgroup.database.schedule.entity

import androidx.room.Entity

@Entity(
    tableName = "schedule_employee"
)
data class ScheduleEmployeeEntity(
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val degree: String?,
    val degreeAbbrev: String?,
    val email: String?,
    val rank: String?,
    val photoLink: String?,
    val calendarId: String?,
    val academicDepartment: List<String>?,
    val jobPositions: String?,
    val chief: Boolean,
    val id: Int?,
    val urlId: String?,
    val fio: String?,
)


