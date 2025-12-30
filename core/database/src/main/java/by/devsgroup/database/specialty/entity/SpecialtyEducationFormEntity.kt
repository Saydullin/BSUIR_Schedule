package by.devsgroup.database.specialty.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "specialty_education_form"
)
data class SpecialtyEducationFormEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val id: Long,
    val name: String,
)


