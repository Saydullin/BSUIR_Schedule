package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.SavedScheduleTable

data class SavedSchedule (
    val id: Int,
    val employee: Employee,
    val group: Group,
    val isGroup: Boolean,
    var lastUpdateTime: Long,
    var isExistExams: Boolean
) {

    fun toSavedScheduleTable() = SavedScheduleTable(
        id = id,
        group = group.toGroupTable(),
        employee = employee.toEmployeeTable(),
        isGroup = isGroup,
        lastUpdateTime = lastUpdateTime,
        isExistExams = isExistExams
    )

}


