package by.devsgroup.database.faculty.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "faculty"
)
data class FacultyEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val id: Long,
    val name: String,
    val abbrev: String,
)
