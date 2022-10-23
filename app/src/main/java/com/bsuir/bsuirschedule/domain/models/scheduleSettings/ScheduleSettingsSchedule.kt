package com.bsuir.bsuirschedule.domain.models.scheduleSettings

data class ScheduleSettingsSchedule (
    var isAutoUpdate: Boolean,
    var isShowEmptyDays: Boolean,
    var isShowPastDays: Boolean,
    var pastDaysNumber: Int,
) {

    companion object {
        val empty = ScheduleSettingsSchedule(
            isAutoUpdate = true,
            isShowEmptyDays = true,
            isShowPastDays = false,
            pastDaysNumber = 1
        )
    }

}


