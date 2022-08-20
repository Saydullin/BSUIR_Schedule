package com.example.bsuirschedule.domain.models

import com.example.bsuirschedule.data.db.entities.DepartmentTable

data class Department(
    val id: Int,
    val name: String,
    val abbrev: String
) {

    fun toDepartmentTable() = DepartmentTable(
        id = id,
        name = name,
        abbr = abbrev
    )

}


