package com.bsuir.bsuirschedule.domain.models.scheduleSettings

data class ScheduleSettingsSchedule (
    var isAutoUpdate: Boolean,
    var isShowShortSchedule: Boolean,
    var isShowEmptyDays: Boolean,
    var isShowPastDays: Boolean,
    var pastDaysNumber: Int,
) {

    companion object {
        val empty = ScheduleSettingsSchedule(
            isAutoUpdate = true,
            isShowShortSchedule = true,
            isShowEmptyDays = true,
            isShowPastDays = false,
            pastDaysNumber = 0
        )
        val fullSchedule = ScheduleSettingsSchedule(
            isAutoUpdate = true,
            isShowShortSchedule = true,
            isShowEmptyDays = true,
            isShowPastDays = true,
            pastDaysNumber = -1
        )
    }

}


