package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.ScheduleSettingsTable

data class ScheduleSettings (
    val id: Int,
    var isAutoUpdate: Boolean,
    var isShowEmptyDays: Boolean,
    var isShowPastDays: Boolean,
    var pastDaysNumber: Int,
    val isShowEmptyExamDays: Boolean,
    val isShowPastExamDays: Boolean,
    val alarmSettings: ScheduleAlarm
) {

    companion object {
        val empty = ScheduleSettings(
            id = -1,
            isAutoUpdate = true,
            isShowEmptyDays = true,
            isShowPastDays = false,
            pastDaysNumber = 1,
            isShowEmptyExamDays = true,
            isShowPastExamDays = false,
            alarmSettings = ScheduleAlarm.empty
        )
    }

    fun toScheduleSettingsTable() = ScheduleSettingsTable(
        id = id,
        isAutoUpdate = isAutoUpdate,
        isShowEmptyDays = isShowEmptyDays,
        isShowPastDays = isShowPastDays,
        isShowEmptyExamDays = isShowEmptyExamDays,
        pastDaysNumber = pastDaysNumber,
        isShowPastExamDays = isShowPastExamDays,
        alarmSettings = alarmSettings.toScheduleAlarmTable()
    )

}


