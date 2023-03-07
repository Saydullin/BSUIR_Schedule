package com.bsuir.bsuirschedule.domain.models.scheduleSettings

data class ScheduleSettingsExams (
    var isShowEmptyDays: Boolean,
    var isShowPastDays: Boolean,
) {

    companion object {
        val empty = ScheduleSettingsExams(
            isShowEmptyDays = false,
            isShowPastDays = true
        )
        val fullSchedule = ScheduleSettingsExams(
            isShowEmptyDays = true,
            isShowPastDays = true
        )
    }

}


