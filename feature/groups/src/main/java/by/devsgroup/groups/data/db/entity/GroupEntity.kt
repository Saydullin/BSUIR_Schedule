package by.devsgroup.groups.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "group",
    indices = [
        Index(
            value = ["name"],
            unique = true
        )
    ]
)
data class GroupEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val id: Int?,
    val name: String?,
    val facultyId: Int?,
    val facultyAbbrev: String?,
    val facultyName: String?,
    val specialityDepartmentEducationFormId: Int?,
    val specialityName: String?,
    val specialityAbbrev: String?,
    val course: Int?,
    val calendarId: String?,
    val educationDegree: Int?,
)


