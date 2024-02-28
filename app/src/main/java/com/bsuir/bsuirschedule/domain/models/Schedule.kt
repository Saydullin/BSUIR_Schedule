package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.ScheduleTable
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class Schedule (
    var id: Int = -1,
    var startDate: String,
    var endDate: String,
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
    var previousSchedules: ArrayList<ScheduleDay>,
    var prevOriginalSchedule: ArrayList<ScheduleDay>,
    var originalSchedule: ArrayList<ScheduleDay>,
    var lastUpdateTime: Long,
    var lastOriginalUpdateTime: Long,
    var selectedSubgroup: Int = 0, // 0 - non selected, show all subgroups
    var settings: ScheduleSettings = ScheduleSettings.empty,
    val currentTerm: String?,
    val currentPeriod: String?,
    val previousTerm: String?,
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
            previousSchedules = ArrayList(),
            prevOriginalSchedule = ArrayList(),
            originalSchedule = ArrayList(),
            lastUpdateTime = 0,
            selectedSubgroup = 0,
            lastOriginalUpdateTime = 0,
            settings = ScheduleSettings.empty,
            currentTerm = null,
            currentPeriod = null,
            previousTerm = null,
        )
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
        val dateFormat = SimpleDateFormat("dd.MM.yyyy").parse(date) ?: return "???"

        return SimpleDateFormat("d MMMM").format(Date(dateFormat.time))
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

    fun toSavedSchedule() = SavedSchedule(
        id = id,
        employee = employee.toEmployee(),
        group = group,
        isGroup = isGroup(),
        lastUpdateTime = lastUpdateTime,
        lastUpdateDate = lastOriginalUpdateTime,
        isUpdatedSuccessfully = false,
        isExistExams = exams.isNotEmpty()
    )

    fun getTitle(): String {
        return if (isGroup()) {
            group.name
        } else {
            employee.getName()
        }
    }

    fun getDescription(): String {
        return if (isGroup()) {
            group.getFacultyAndSpecialityAbbr()
        } else {
            employee.getRankAndDegree()
        }
    }

    fun getImage(): String {
        return if (isGroup()) {
            ""
        } else {
            employee.photoLink
        }
    }

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
        originalSchedule = originalSchedule,
        prevOriginalSchedule = prevOriginalSchedule,
        schedules = schedules,
        previousSchedules = previousSchedules,
        lastUpdateTime = lastUpdateTime,
        lastOriginalUpdateTime = lastOriginalUpdateTime,
        settings = settings,
        currentTerm = currentTerm ?: "",
        currentPeriod = currentPeriod ?: "",
        previousTerm = previousTerm ?: "",
    )

}


