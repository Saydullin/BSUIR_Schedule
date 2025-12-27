package by.devsgroup.database.employees.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "employee",
    indices = [
        Index(
            value = ["id"],
            unique = true
        ),
        Index(
            value = ["departmentKeyId"],
            unique = true
        )
    ]
)
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val departmentKeyId: String,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val degree: String?,
    val rank: String?,
    val photoLink: String?,
    val calendarId: String?,
    val id: Int?,
    val urlId: String?,
    val fio: String?,
)
