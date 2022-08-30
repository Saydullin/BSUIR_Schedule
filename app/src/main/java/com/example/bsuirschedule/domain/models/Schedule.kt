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
    var examsSchedule: ArrayList<ScheduleDay>,
    var schedules: ArrayList<ScheduleDay>,
    val lastUpdateTime: Long,
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
            examsSchedule = ArrayList(),
            schedules = ArrayList(),
            lastUpdateTime = 0,
            selectedSubgroup = 0
        )
    }

    fun isGroup() = group.id != -1

    fun isNotExamsExist(): Boolean {
        return startExamsDate.isEmpty() ||
                endExamsDate.isEmpty() ||
                exams.isEmpty()
    }

    fun isNotEmpty(): Boolean {
        return id != -1
    }

    fun toSavedSchedule() = SavedSchedule(
        id = id,
        employee = employee.toEmployee(),
        group = group,
        isGroup = isGroup(),
        lastUpdateTime = lastUpdateTime,
        isExistExams = exams.isNotEmpty()
    )

}


