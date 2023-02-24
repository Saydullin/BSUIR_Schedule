package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class GroupSchedule (
    var id: Int,
    val startDate: String?,
    val endDate: String?,
    val startExamsDate: String?,
    val endExamsDate: String?,
    @SerializedName("studentGroupDto") val group: Group?,
    @SerializedName("employeeDto") val employee: EmployeeSubject?,
    var subgroups: List<Int>?,
    val schedules: GroupScheduleWeek? = GroupScheduleWeek.empty,
    val exams: ArrayList<ScheduleSubject>? = ArrayList(),
    var examsSchedule: ArrayList<ScheduleDay>? = ArrayList(),
    var lastUpdateTime: Long? = Date().time,
) {

    companion object {
        val empty = GroupSchedule(
            id = -1,
            startDate = "",
            endDate = "",
            startExamsDate = "",
            endExamsDate = "",
            group = Group.empty,
            employee = EmployeeSubject.empty,
            subgroups = ArrayList(),
            exams = ArrayList(),
            examsSchedule = ArrayList(),
            lastUpdateTime = Date().time,
            schedules = GroupScheduleWeek.empty
        )
    }

    fun toSchedule() = Schedule(
        id = id,
        startDate = startDate ?: "",
        endDate = endDate ?: "",
        startExamsDate = startExamsDate ?: "",
        endExamsDate = endExamsDate ?: "",
        group = group ?: Group.empty,
        employee = employee ?: EmployeeSubject.empty,
        isGroup = group?.id == -1,
        subgroups = subgroups ?: listOf(),
        exams = exams ?: ArrayList(),
        examsSchedule = examsSchedule ?: ArrayList(),
        updateHistorySchedule = ArrayList(),
        subjectNow = null,
        lastUpdateTime = lastUpdateTime ?: Date().time,
        lastUpdateDate = "",
        schedules = ArrayList(),
        settings = ScheduleSettings.empty
    )

}


