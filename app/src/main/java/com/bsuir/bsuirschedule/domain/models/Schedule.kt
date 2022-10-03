package com.bsuir.bsuirschedule.domain.models

import java.text.SimpleDateFormat
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
    var selectedSubgroup: Int = 0 // 0 - non selected, show all subgroups
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

    fun getLastUpdateText(): String {
        val date = Date()
        val dateFormat = SimpleDateFormat("d MMMM, H:mm")

        date.time = lastUpdateTime
        return dateFormat.format(date)
    }

    fun getDateText(date: String): String {
        if (date.isNullOrEmpty()) return "???"
        val dateFormat = SimpleDateFormat("dd.MM.yyyy").parse(date)
        if (dateFormat != null) {
            return SimpleDateFormat("d MMMM").format(Date(dateFormat.time))
        }

        return "???"
    }

    fun isGroup() = group.id != -1

    fun isExamsNotExist(): Boolean {
        return startExamsDate.isEmpty() ||
                endExamsDate.isEmpty() ||
                exams.isEmpty()
    }

    fun isScheduleNotExist(): Boolean {
        return startDate.isEmpty() ||
                endDate.isEmpty() ||
                schedules.isEmpty()
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


