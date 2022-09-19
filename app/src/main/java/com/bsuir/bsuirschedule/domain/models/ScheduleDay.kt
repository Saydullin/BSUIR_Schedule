package com.bsuir.bsuirschedule.domain.models

data class ScheduleDay (
    val id: Int = -1,
    val date: String,
    val weekDayName: String,
    val weekDayNumber: Int,
    val weekNumber: Int,
    var schedule: ArrayList<ScheduleSubject> = ArrayList()
) {

    companion object {
        val empty = ScheduleDay(
            id = -1,
            date = "",
            weekDayName = "",
            weekDayNumber = 0,
            weekNumber = 0,
            schedule = ArrayList()
        )
    }

}


