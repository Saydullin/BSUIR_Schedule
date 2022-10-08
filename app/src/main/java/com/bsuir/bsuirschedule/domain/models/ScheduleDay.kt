package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.GroupScheduleSubjectTable
import com.bsuir.bsuirschedule.data.db.entities.ScheduleDayTable

data class ScheduleDay (
    val id: Int = -1,
    var date: String,
    var dateUnixTime: Long,
    var weekDayTitle: String,
    var weekDayNumber: Int,
    var weekNumber: Int,
    var schedule: ArrayList<ScheduleSubject> = ArrayList()
) {

    companion object {
        val empty = ScheduleDay(
            id = -1,
            date = "",
            dateUnixTime = 0,
            weekDayTitle = "",
            weekDayNumber = 0,
            weekNumber = 0,
            schedule = ArrayList()
        )
    }

    fun toScheduleDayTable() = ScheduleDayTable(
        id = id,
        date = date,
        dateUnixTime = dateUnixTime,
        weekDayName = weekDayTitle,
        weekDayNumber = weekDayNumber,
        weekNumber = weekNumber,
        schedule = schedule.map { it.toGroupScheduleSubjectTable() } as ArrayList<GroupScheduleSubjectTable>
    )

    fun weekNumberString() = weekNumber.toString()

    fun weekDayNameUpperFirstLetter() = weekDayTitle.replaceFirstChar { it.uppercase() }

}


