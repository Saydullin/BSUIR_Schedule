package com.example.bsuirschedule.domain.models

import com.example.bsuirschedule.data.db.entities.GroupScheduleTable
import com.google.gson.annotations.SerializedName

data class GroupSchedule (
    var id: Int,
    val startDate: String?,
    val endDate: String?,
    val startExamsDate: String?,
    val endExamsDate: String?,
    @SerializedName("studentGroupDto") val group: Group?,
    @SerializedName("employeeDto") val employee: EmployeeSubject?,
    val subgroups: List<Int>?,
    val schedules: GroupScheduleWeek,
    val exams: ArrayList<ScheduleSubject>,
    val selectedSubgroup: Int = 0 // 0 - non selected, show all subgroups
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
            schedules = GroupScheduleWeek.empty
        )
    }

    fun isNotSuitable(): Boolean {
        return startDate.isNullOrEmpty() ||
                endDate.isNullOrEmpty()
    }

    fun toGroupScheduleTable() = GroupScheduleTable(
        id = id,
        startDate = startDate ?: "",
        endDate = endDate ?: "",
        startExamsDate = startExamsDate ?: "",
        group = group?.toGroupTable() ?: Group.empty.toGroupTable(),
        employee = employee?.toEmployeeTable() ?: EmployeeSubject.empty.toEmployeeTable(),
        endExamsDate = endExamsDate ?: "",
        subgroups = subgroups ?: ArrayList(),
        schedules = schedules.toGroupScheduleWeekTable(),
        exams = exams,
        selectedSubgroup = selectedSubgroup
    )

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
        exams = exams,
        schedules = ArrayList(),
    )

}


