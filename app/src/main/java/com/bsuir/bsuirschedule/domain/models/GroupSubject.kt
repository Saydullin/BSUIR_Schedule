package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.GroupSubjectTable

data class GroupSubject(
    val specialityName: String,
    val specialityCode: String,
    val numberOfStudents: Int,
    val name: String,
    val educationDegree: Int
) {

    fun toGroupSubjectTable() = GroupSubjectTable(
        specialityName = specialityName,
        specialityCode = specialityCode,
        numberOfStudents = numberOfStudents,
        name = name,
        educationDegree = educationDegree
    )

}
