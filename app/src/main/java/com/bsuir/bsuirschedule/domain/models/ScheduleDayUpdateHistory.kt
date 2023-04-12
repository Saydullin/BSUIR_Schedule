package com.bsuir.bsuirschedule.domain.models

data class ScheduleDayUpdateHistory(
    val id: Int,
    val scheduleDay: ScheduleDay,
    var scheduleActions: ArrayList<ScheduleUpdatedAction>,
) {

    companion object {
        val empty = ScheduleDayUpdateHistory(
            id = -1,
            scheduleDay = ScheduleDay.empty,
            scheduleActions = ArrayList(),
        )
    }

}