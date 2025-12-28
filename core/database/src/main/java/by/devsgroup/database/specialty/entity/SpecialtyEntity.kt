package by.devsgroup.database.specialty.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "specialty"
)
data class SpecialtyEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val id: Long,
    val name: String,
    val abbrev: String,
    val facultyId: Long,
    val code: String,
    @Embedded(
        prefix = "education_form_"
    )
    val educationForm: SpecialtyEducationFormEntity,
)