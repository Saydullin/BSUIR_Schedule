package com.bsuir.bsuirschedule.domain.manager.schedule.component

import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings

class ScheduleSettingsSubgroup(
    private val scheduleSettings: ScheduleSettings,
    private val scheduleDays: ArrayList<ScheduleDay>,
) {

    fun execute(): ArrayList<ScheduleDay> {

        return filterBySubgroup(
            scheduleSettings.subgroup.selectedNum
        )
    }

    private fun filterBySubgroup(
        subgroup: Int
    ): ArrayList<ScheduleDay> {
        val scheduleList = ArrayList<ScheduleDay>()
        if (subgroup == 0) return scheduleDays

        scheduleDays.map { day ->
            val subjects = day.schedule.filter { it.getEditedOrNumSubgroup() == 0 || it.getEditedOrNumSubgroup() == subgroup }
            day.schedule = subjects as ArrayList<ScheduleSubject>
            scheduleList.add(day)
        }

        return scheduleList
    }

}