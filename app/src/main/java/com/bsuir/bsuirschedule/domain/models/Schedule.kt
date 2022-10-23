package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.ScheduleTable
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class Schedule (
    var id: Int = -1,
    val startDate: String,
    val endDate: String,
    val startExamsDate: String,
    val endExamsDate: String,
    val group: Group,
    val employee: EmployeeSubject,
    var subgroups: List<Int>,
    val isGroup: Boolean?,
    var exams: ArrayList<ScheduleSubject>,
    var examsSchedule: ArrayList<ScheduleDay>,
    var subjectNow: ScheduleSubject?,
    var schedules: ArrayList<ScheduleDay>,
    val lastUpdateTime: Long,
    var selectedSubgroup: Int = 0, // 0 - non selected, show all subgroups
    val settings: ScheduleSettings = ScheduleSettings.empty
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
            subjectNow = null,
            schedules = ArrayList(),
            lastUpdateTime = 0,
            selectedSubgroup = 0,
            settings = ScheduleSettings.empty
        )
    }

    fun isNotExistExams(): Boolean {
        return startExamsDate.isEmpty() ||
                endExamsDate.isEmpty() ||
                exams.isEmpty()
    }

    fun isNotExistSchedule(): Boolean {
        return startDate.isEmpty() ||
                endDate.isEmpty() ||
                schedules.isEmpty()
    }

    fun getLastUpdateText(): String {
        val date = Date()
        val dateFormat = SimpleDateFormat("d MMMM, H:mm")

        date.time = lastUpdateTime
        return dateFormat.format(date)
    }

    fun getDateText(date: String): String {
        if (date.isEmpty()) return "???"
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

    fun toScheduleTable() = ScheduleTable(
        id = id,
        startDate = startDate,
        endDate = endDate,
        startExamsDate = startExamsDate,
        endExamsDate = endExamsDate,
        group = group.toGroupTable(),
        employee = employee.toEmployeeTable(),
        subgroups = subgroups,
        isGroup = isGroup,
        exams = exams,
        examsSchedule = examsSchedule,
        schedules = schedules,
        lastUpdateTime = lastUpdateTime,
        selectedSubgroup = selectedSubgroup,
        settings = settings
    )

}


