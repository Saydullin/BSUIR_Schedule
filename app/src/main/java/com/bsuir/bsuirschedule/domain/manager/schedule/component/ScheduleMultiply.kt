package com.bsuir.bsuirschedule.domain.manager.schedule.component

import android.util.Log
import com.bsuir.bsuirschedule.domain.models.Holiday
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.utils.CalendarDate
import com.bsuir.bsuirschedule.domain.utils.ScheduleController

class ScheduleMultiply(
    private val scheduleDays: ArrayList<ScheduleDay>,
    private val holidays: List<Holiday>,
    private val currentWeekNumber: Int,
    private val startDate: String,
    private val endDate: String,
) {

    fun execute(): ArrayList<ScheduleDay> {
        val calendarDate = CalendarDate(
            startDate = startDate,
            weekNumber = currentWeekNumber
        )
        var daysCounter = 0
        val multipliedScheduleDays = ArrayList<ScheduleDay>()

        while (!calendarDate.isEqualDate(endDate) && calendarDate.getIncDayCounter() < ScheduleController.DAYS_LIMIT) {
            calendarDate.incDate(daysCounter)
            val currentTimeInMillis = calendarDate.getDateInMillis()
            val weekNumber = calendarDate.getWeekNum()
            val weekDayNumber = calendarDate.getWeekDayNumber()
            val weekNumberDays = scheduleDays.filter { it.weekDayNumber == weekDayNumber }
            val weekNumberDaysCopy = weekNumberDays.map { it.copy() }
            val holidayMatch = holidays.find { calendarDate.isHoliday(it.date) }
            if (holidayMatch != null) {
                multipliedScheduleDays.add(
                    ScheduleDay(
                        date = calendarDate.getDateStatus(),
                        dateInMillis = calendarDate.getDateInMillis(),
                        weekDayTitle = calendarDate.getWeekDayTitle(),
                        weekDayNumber = weekDayNumber,
                        weekNumber = weekNumber,
                        dayDescription = holidayMatch.title,
                        schedule = ArrayList(),
                    ))
            } else {
                if (weekNumberDaysCopy.isEmpty()) {
                    multipliedScheduleDays.add(
                        ScheduleDay(
                            date = calendarDate.getDateStatus(),
                            dateInMillis = calendarDate.getDateInMillis(),
                            weekDayTitle = calendarDate.getWeekDayTitle(),
                            weekDayNumber = weekDayNumber,
                            weekNumber = weekNumber,
                            schedule = ArrayList(),
                        ))
                }
                weekNumberDaysCopy.map { scheduleDay ->
                    val subjects = scheduleDay.schedule.filter { subject ->
                        if (subject.startLessonDate.isNullOrEmpty() || subject.endLessonDate.isNullOrEmpty()) {
                            weekNumber in (subject.weekNumber ?: ArrayList())
                        } else {
                            val startMillis = calendarDate.getDateInMillis(subject.startLessonDate)
                            val endMillis = calendarDate.getDateInMillis(subject.endLessonDate)
                            weekNumber in (subject.weekNumber ?: ArrayList()) &&
                                    currentTimeInMillis >= calendarDate.resetMillisTime(startMillis) &&
                                    currentTimeInMillis <= calendarDate.resetMillisTime(endMillis)
                        }
                    } as ArrayList<ScheduleSubject>
                    val subjectsCopy = subjects.map { it.copy() }
                    multipliedScheduleDays.add(
                        ScheduleDay(
                            date = calendarDate.getDateStatus(),
                            dateInMillis = calendarDate.getDateInMillis(),
                            weekDayTitle = calendarDate.getWeekDayTitle(),
                            weekDayNumber = weekDayNumber,
                            weekNumber = weekNumber,
                            schedule = subjectsCopy as ArrayList<ScheduleSubject>
                        ))
                }
            }
            daysCounter++
        }
        Log.e("sady", "daysCounter: $daysCounter, calendarDate: ${calendarDate.getIncDayCounter()}")

        return multipliedScheduleDays
    }

}


