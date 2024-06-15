package com.bsuir.bsuirschedule.domain.manager.schedule.component

import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettingsSchedule
import com.bsuir.bsuirschedule.domain.utils.CalendarDate

class ScheduleSettingsPastDays(
    private val scheduleDays: ArrayList<ScheduleDay>,
    private val scheduleSettings: ScheduleSettingsSchedule,
    private val startDate: String,
) {

    fun execute(): ArrayList<ScheduleDay> {
        if (scheduleDays.isEmpty()) return scheduleDays

        val filteredDays = ArrayList<ScheduleDay>()

        if (!scheduleSettings.isShowPastDays) {
            filteredDays.addAll(
                filterActualSchedule(
                    scheduleDays,
                    0,
                    startDate
                )
            )
        } else {
            filteredDays.addAll(
                filterActualSchedule(
                    scheduleDays,
                    scheduleSettings.pastDaysNumber,
                    startDate
                )
            )
        }

        return filteredDays
    }

    private fun filterActualSchedule(schedule: ArrayList<ScheduleDay>, preDays: Int = 0, startDate: String): ArrayList<ScheduleDay> {
        val calendarDate = if (preDays == -1) {
            CalendarDate(startDate = startDate)
        } else {
            CalendarDate(startDate = CalendarDate.TODAY_DATE)
        }
        if (preDays > 0) {
            calendarDate.minusDays(preDays)
        }

        val scheduleDays = schedule.filter { day ->
            day.dateInMillis >= calendarDate.getDateInMillis()
        }

        return scheduleDays as ArrayList<ScheduleDay>
    }

}