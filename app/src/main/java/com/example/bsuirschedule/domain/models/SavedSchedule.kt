package com.example.bsuirschedule.domain.models

import com.example.bsuirschedule.data.db.entities.SavedScheduleTable
import java.text.SimpleDateFormat
import java.util.*

data class SavedSchedule (
    val id: Int,
    val employee: Employee,
    val group: Group,
    val isGroup: Boolean,
    var lastUpdateTime: Long,
    var isExistExams: Boolean
) {

    fun getLastUpdateText(): String {
        val date = Date()
        val dateFormat = SimpleDateFormat("d MMMM, H:mm")

        date.time = lastUpdateTime
        return dateFormat.format(date)
    }

    fun toSavedScheduleTable() = SavedScheduleTable(
        id = id,
        group = group.toGroupTable(),
        employee = employee.toEmployeeTable(),
        isGroup = isGroup,
        lastUpdateTime = lastUpdateTime,
        isExistExams = isExistExams
    )

}


