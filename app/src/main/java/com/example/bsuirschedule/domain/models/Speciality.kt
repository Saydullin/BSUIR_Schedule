package com.example.bsuirschedule.domain.models

import com.example.bsuirschedule.data.db.entities.SpecialityTable

data class Speciality (
    val id: Int,
    val name: String,
    val abbrev: String,
    val educationForm: EducationForm,
    val facultyId: Int,
    val code: String
) {

    companion object {
        val empty = Speciality(
            id = -1,
            name = "",
            abbrev = "",
            educationForm = EducationForm.empty,
            facultyId = -1,
            code = ""
        )
    }

    fun toSpecialityTable() = SpecialityTable(
        id = id,
        name = name,
        abbrev = abbrev,
        educationForm = educationForm.toEducationFormTable(),
        facultyId = facultyId,
        code = code
    )

}


