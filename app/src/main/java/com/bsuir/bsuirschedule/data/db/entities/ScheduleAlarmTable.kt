package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsuir.bsuirschedule.domain.models.ScheduleAlarm

@Entity
data class ScheduleAlarmTable (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val createDate: Long = 0L,
    @ColumnInfo val isTurnOn: Boolean,
    @ColumnInfo val wakeUpTime: Long,
    @ColumnInfo val weekNumbers: List<Int>,
    @ColumnInfo val weekDayNumbers: List<Int>,
    @ColumnInfo val isOneTime: Boolean,
    @ColumnInfo val music: String
) {

    fun toScheduleAlarm() = ScheduleAlarm(
        id = id,
        createDate = createDate,
        isTurnOn = isTurnOn,
        wakeUpTime = wakeUpTime,
        weekNumbers = weekNumbers,
        weekDayNumbers = weekDayNumbers,
        isOneTime = isOneTime,
        music = music
    )

}


