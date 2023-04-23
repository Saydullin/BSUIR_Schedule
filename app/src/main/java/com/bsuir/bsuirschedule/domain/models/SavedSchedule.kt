package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.SavedScheduleTable

data class SavedSchedule (
    val id: Int,
    val employee: Employee,
    val group: Group,
    val isGroup: Boolean,
    var lastUpdateTime: Long,
    var lastUpdateDate: Long,
    var isUpdatedSuccessfully: Boolean,
    var isExistExams: Boolean
) {

    fun getTitle(): String {
        return if (isGroup) {
            group.getTitleOrName()
        } else {
            employee.getTitleOrFullName()
        }
    }

    fun getName(): String {
        return if (isGroup) {
            group.name
        } else {
            employee.getName()
        }
    }

    fun toSavedScheduleTable() = SavedScheduleTable(
        id = id,
        group = group.toGroupTable(),
        employee = employee.toEmployeeTable(),
        isGroup = isGroup,
        lastUpdateTime = lastUpdateTime,
        lastOriginalUpdateTime = lastUpdateDate,
        isUpdatedSuccessfully = isUpdatedSuccessfully,
        isExistExams = isExistExams
    )

}


