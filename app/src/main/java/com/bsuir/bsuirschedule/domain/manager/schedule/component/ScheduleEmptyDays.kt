package com.bsuir.bsuirschedule.domain.manager.schedule.component

import com.bsuir.bsuirschedule.domain.models.ScheduleDay

class ScheduleEmptyDays(
    private val scheduleDays: ArrayList<ScheduleDay>,
) {

    fun execute(): ArrayList<ScheduleDay> {
       val emptyDaysClear = scheduleDays.filter {
           it.schedule.isNotEmpty()
       }

        return emptyDaysClear as ArrayList<ScheduleDay>
    }

}