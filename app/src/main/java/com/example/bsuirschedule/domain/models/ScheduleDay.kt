package com.example.bsuirschedule.domain.models

data class ScheduleDay (
    var id: Int = -1,
    var date: String = "",
    var weekDayName: String = "",
    var weekDayNumber: Int = 0,
    var lessonsAmount: Int = 0,
    var schedule: ArrayList<ScheduleSubject> = ArrayList()
) {

    companion object {
        val empty = ScheduleDay(
            id = -1,
            date = "",
            weekDayName = "",
            weekDayNumber = 0,
            lessonsAmount = 0,
            schedule = ArrayList()
        )
    }

}


