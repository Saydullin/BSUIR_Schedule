package com.example.bsuirschedule.domain.models

import com.example.bsuirschedule.data.db.entities.EducationFormTable

data class EducationForm (
    val id: Int,
    val name: String
) {

    companion object {
        val empty = EducationForm(
            id = -1,
            name = ""
        )
    }

    fun toEducationFormTable() = EducationFormTable(
        id = id,
        name = name
    )

}


