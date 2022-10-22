package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsuir.bsuirschedule.domain.models.ScheduleAlarm
import com.bsuir.bsuirschedule.domain.models.ScheduleSettings

@Entity
data class ScheduleSettingsTable (
    @PrimaryKey val id: Int,
    @ColumnInfo val isAutoUpdate: Boolean,
    @ColumnInfo val isShowEmptyDays: Boolean,
    @ColumnInfo val isShowPastDays: Boolean,
    @ColumnInfo val pastDaysNumber: Int,
    @ColumnInfo val isShowEmptyExamDays: Boolean,
    @ColumnInfo val isShowPastExamDays: Boolean,
    @Embedded(prefix = "alarm_") val alarmSettings: ScheduleAlarmTable
) {

    fun toScheduleSettings() = ScheduleSettings(
        id = id,
        isAutoUpdate = isAutoUpdate,
        isShowEmptyDays = isShowEmptyDays,
        isShowPastDays = isShowPastDays,
        pastDaysNumber = pastDaysNumber,
        isShowEmptyExamDays = isShowEmptyExamDays,
        isShowPastExamDays = isShowPastExamDays,
        alarmSettings = alarmSettings.toScheduleAlarm()
    )

}


