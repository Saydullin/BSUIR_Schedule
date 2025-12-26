package by.devsgroup.employees.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "employee",
    indices = [
        Index(
            value = ["id"],
            unique = true
        )
    ]
)
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val degree: String?,
    val rank: String?,
    val photoLink: String?,
    val calendarId: String?,
    val academicDepartment: List<String>?,
    val id: Int?,
    val urlId: String?,
    val fio: String?,
)
