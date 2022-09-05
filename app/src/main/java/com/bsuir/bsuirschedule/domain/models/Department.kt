package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.DepartmentTable

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


