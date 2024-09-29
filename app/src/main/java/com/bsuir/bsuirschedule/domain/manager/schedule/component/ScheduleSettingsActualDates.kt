package com.bsuir.bsuirschedule.domain.manager.schedule.component

import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettingsSchedule
import com.bsuir.bsuirschedule.domain.utils.CalendarDate

class ScheduleSettingsActualDates(
    private val scheduleSettings: ScheduleSettingsSchedule,
    private val scheduleDays: ArrayList<ScheduleDay>,
) {

    fun execute(): ArrayList<ScheduleDay> {

        return setActualDateStatuses(
            isShowPastDays = scheduleSettings.isShowPastDays,
        )
    }

    private fun setActualDateStatuses(
        isShowPastDays: Boolean,
    ): ArrayList<ScheduleDay> {
        val calendarDate = CalendarDate(startDate = CalendarDate.TODAY_DATE)

        val todayStartIndex = scheduleDays.indexOfFirst { it.dateInMillis == calendarDate.getDateInMillis() }
        if (todayStartIndex == -1) return scheduleDays

        if (isShowPastDays && todayStartIndex > 0) {
            calendarDate.minusDays(1)
            scheduleDays[todayStartIndex - 1].date = calendarDate.getDateStatus()
        }

        for ((dateCounter, i) in (todayStartIndex..todayStartIndex + 2).withIndex()) {
            if (i >= scheduleDays.size) break
            calendarDate.incDate(dateCounter)
            scheduleDays[i].date = calendarDate.getDateStatus()
        }

        return scheduleDays
    }

}


