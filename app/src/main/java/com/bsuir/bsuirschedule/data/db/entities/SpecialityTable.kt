package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsuir.bsuirschedule.domain.models.Speciality

@Entity
data class SpecialityTable(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val abbrev: String,
    @Embedded(prefix = "education_form_") val educationForm: EducationFormTable,
    @ColumnInfo val facultyId: Int,
    @ColumnInfo val code: String
) {

    fun toSpeciality() = Speciality(
        id = id,
        name = name,
        abbrev = abbrev,
        educationForm = educationForm.toEducationForm(),
        facultyId = facultyId,
        code = code
    )

}


