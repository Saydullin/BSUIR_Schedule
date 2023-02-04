package com.bsuir.bsuirschedule.domain.models

data class WidgetSchedule (
    val isScheduleEmpty: Boolean,
    val schedule: Schedule?,
    val activeScheduleDay: ScheduleDay?
) {

    companion object {
        val notExist = WidgetSchedule(
            isScheduleEmpty = false,
            schedule = null,
            activeScheduleDay = null
        )
    }

}