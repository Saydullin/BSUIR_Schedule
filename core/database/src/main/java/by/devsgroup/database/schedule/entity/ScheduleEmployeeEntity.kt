package by.devsgroup.database.schedule.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedule_employee"
)
data class ScheduleEmployeeEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val id: Long?,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val degree: String?,
    val degreeAbbrev: String?,
    val email: String?,
    val rank: String?,
    val photoLink: String?,
    val calendarId: String?,
    val chief: Boolean?,
    val urlId: String?,
)


