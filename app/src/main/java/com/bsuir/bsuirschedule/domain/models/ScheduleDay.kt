package com.bsuir.bsuirschedule.domain.models

data class ScheduleDay (
    val id: Int = -1,
    var date: String,
    var dateUnixTime: Long,
    var weekDayName: String,
    var weekDayNumber: Int,
    var weekNumber: Int,
    var schedule: ArrayList<ScheduleSubject> = ArrayList()
) {

    companion object {
        val empty = ScheduleDay(
            id = -1,
            date = "",
            dateUnixTime = 0,
            weekDayName = "",
            weekDayNumber = 0,
            weekNumber = 0,
            schedule = ArrayList()
        )
    }

    fun weekNumberString() = weekNumber.toString()

    fun weekDayNameUpperFirstLetter() = weekDayName.replaceFirstChar { it.uppercase() }

}


