package com.bsuir.bsuirschedule.domain.models

data class ScheduleDayUpdateHistory(
    val id: Int,
    val scheduleDay: ScheduleDay,
    var scheduleSubjects: ArrayList<ScheduleSubjectHistory>,
) {

    companion object {
        val empty = ScheduleDayUpdateHistory(
            id = -1,
            scheduleDay = ScheduleDay.empty,
            scheduleSubjects = ArrayList(),
        )
    }

}