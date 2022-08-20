package com.example.bsuirschedule.domain.models

import java.util.*
import kotlin.collections.ArrayList

data class Schedule (
    val id: Int,
    val startDate: String,
    val endDate: String,
    val startExamsDate: String,
    val endExamsDate: String,
    val group: Group,
    val employee: EmployeeSubject,
    val subgroups: List<Int>,
    val isGroup: Boolean?,
    var exams: ArrayList<ScheduleSubject>,
    var schedules: ArrayList<ScheduleDay>,
    val selectedSubgroup: Int = 0 // 0 - non selected, show all subgroups
) {

    companion object {
        val empty = Schedule(
            id = -1,
            startDate = "",
            endDate = "",
            startExamsDate = "",
            endExamsDate = "",
            group = Group.empty,
            employee = EmployeeSubject.empty,
            subgroups = listOf(),
            isGroup = null,
            exams = ArrayList(),
            schedules = ArrayList(),
            selectedSubgroup = 0
        )
    }

    fun toSavedSchedule() = SavedSchedule(
        id = id,
        employee = employee.toEmployee(),
        group = group,
        isGroup = isGroup ?: false,
        lastUpdateTime = Date().time,
        isExistExams = exams.isNotEmpty()
    )

}


