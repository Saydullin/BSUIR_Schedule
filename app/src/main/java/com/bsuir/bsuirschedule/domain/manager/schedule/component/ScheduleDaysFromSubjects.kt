package com.bsuir.bsuirschedule.domain.manager.schedule.component

import android.util.Log
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.utils.CalendarDate
import com.bsuir.bsuirschedule.domain.utils.ScheduleController

class ScheduleDaysFromSubjects(
    private val scheduleSubjects: ArrayList<ScheduleSubject>,
    private val currentWeekNumber: Int,
    private val startDate: String?,
    private val endDate: String?,
) {

    fun execute(): ArrayList<ScheduleDay> {
        Log.e("sady", "EXAM DATES $startDate, $endDate")
        if (startDate.isNullOrEmpty() || endDate.isNullOrEmpty()) return arrayListOf()

        val calendarDate = CalendarDate(
            startDate = startDate,
            weekNumber = currentWeekNumber
        )
        val scheduleDays = ArrayList<ScheduleDay>()
        var daysCounter = 0

        while (!calendarDate.isEqualDate(endDate) && daysCounter < ScheduleController.DAYS_LIMIT) {
            val fullDate = calendarDate.getFullDate(daysCounter)
            val subjectsDay = scheduleSubjects.filter { subject ->
                subject.dateLesson == fullDate
            }
            scheduleDays.add(
                ScheduleDay(
                    date = calendarDate.getDate(),
                    dateInMillis = calendarDate.getDateInMillis(),
                    weekDayTitle = calendarDate.getWeekDayTitle(),
                    weekDayNumber = calendarDate.getWeekDayNumber(),
                    weekNumber = calendarDate.getWeekNumber(),
                    schedule = subjectsDay as ArrayList<ScheduleSubject>
                )
            )
            daysCounter++
        }

        return scheduleDays
    }

}


