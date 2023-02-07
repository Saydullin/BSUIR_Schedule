package com.bsuir.bsuirschedule.domain.models

data class ScheduleDay (
    val id: Int = -1,
    var date: String,
    var dateInMillis: Long,
    var weekDayTitle: String,
    var weekDayNumber: Int,
    var weekNumber: Int,
    var schedule: ArrayList<ScheduleSubject> = ArrayList()
) {

    companion object {
        val empty = ScheduleDay(
            id = -1,
            date = "",
            dateInMillis = 0,
            weekDayTitle = "",
            weekDayNumber = 0,
            weekNumber = 0,
            schedule = ArrayList()
        )
    }

    fun weekNumberString() = weekNumber.toString()

    fun weekDayNameUpperFirstLetter() = weekDayTitle.replaceFirstChar { it.uppercase() }

}


