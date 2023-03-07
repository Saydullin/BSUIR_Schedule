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
            weekNumber = -1,
            schedule = ArrayList()
        )
    }

    fun weekNumberString(): String {
        return if (weekNumber == -1) {
            ""
        } else {
            weekNumber.toString()
        }
    }

    fun weekDayNameUpperFirstLetter() = weekDayTitle.replaceFirstChar { it.uppercase() }

    fun toScheduleDayUpdatedHistory(
        status: SubjectHistoryStatus = SubjectHistoryStatus.NOTHING,
        scheduleSubjects: ArrayList<ScheduleSubjectHistory>? = null
    ) = ScheduleDayUpdateHistory(
        id = id,
        scheduleDay = this,
        scheduleSubjects = scheduleSubjects ?: schedule.map { it.toSubjectHistory(status) } as ArrayList<ScheduleSubjectHistory>,
    )

}


