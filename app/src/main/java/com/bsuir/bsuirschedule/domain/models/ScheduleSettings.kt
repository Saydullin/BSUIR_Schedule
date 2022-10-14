package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.ScheduleSettingsTable

data class ScheduleSettings (
   val id: Int,
   val isAutoUpdate: Boolean,
   val isShowEmptyDays: Boolean,
   val isShowPastDays: Boolean,
   val isShowEmptyExamDays: Boolean,
   val isShowPastExamDays: Boolean,
   val alarmSettings: ScheduleAlarm
) {

    companion object {
        val empty = ScheduleSettings(
            id = -1,
            isAutoUpdate = false,
            isShowEmptyDays = false,
            isShowPastDays = false,
            isShowEmptyExamDays = false,
            isShowPastExamDays = false,
            ScheduleAlarm.empty
        )
    }

    fun toScheduleSettingsTable() = ScheduleSettingsTable(
        id = id,
        isAutoUpdate = isAutoUpdate,
        isShowEmptyDays = isShowEmptyDays,
        isShowPastDays = isShowPastDays,
        isShowEmptyExamDays = isShowEmptyExamDays,
        isShowPastExamDays = isShowPastExamDays,
        alarmSettings = alarmSettings.toScheduleAlarmTable()
    )

}