package com.example.bsuirschedule.domain.models

import com.example.bsuirschedule.data.db.entities.FacultyTable

data class Faculty(
    val id: Int,
    val name: String,
    val abbrev: String
) {

    companion object {
        val empty = Faculty(
            id = -1,
            name = "",
            abbrev = ""
        )
    }

    fun toFacultyTable() = FacultyTable(
        id = id,
        name = name,
        abbr = abbrev
    )

}


