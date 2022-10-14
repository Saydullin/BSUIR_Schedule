package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.ScheduleAlarmTable

data class ScheduleAlarm (
    val id: Int,
    val createDate: Long,
    val isTurnOn: Boolean,
    val wakeUpTime: Long,
    val weekNumbers: List<Int>,
    val weekDayNumbers: List<Int>,
    val isOneTime: Boolean,
    val music: String
) {

   companion object {
       val empty = ScheduleAlarm(
           -1,
               0L,
               false,
               0L,
               listOf(),
               listOf(),
               false,
               ""
       )
   }

   fun toScheduleAlarmTable() = ScheduleAlarmTable(
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