package com.bsuir.bsuirschedule.domain.utils

import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject

class ScheduleController {

    fun getNormalSchedule(groupSchedule: GroupSchedule): Schedule {
        val weekDays = arrayListOf(
            initScheduleDay(groupSchedule.schedules?.monday ?: ArrayList(), 1),
            initScheduleDay(groupSchedule.schedules?.tuesday ?: ArrayList(), 2),
            initScheduleDay(groupSchedule.schedules?.wednesday ?: ArrayList(), 3),
            initScheduleDay(groupSchedule.schedules?.thursday ?: ArrayList(), 4),
            initScheduleDay(groupSchedule.schedules?.friday ?: ArrayList(), 5),
            initScheduleDay(groupSchedule.schedules?.saturday ?: ArrayList(), 6),
            initScheduleDay(groupSchedule.schedules?.sunday ?: ArrayList(), 7),
        )

        val schedule = groupSchedule.toSchedule()
        schedule.schedules = weekDays

        return schedule
    }

    private fun initScheduleDay(subjects: ArrayList<ScheduleSubject>, dayNumber: Int): ScheduleDay {
        val scheduleDay = ScheduleDay.empty
        scheduleDay.weekDayNumber = dayNumber
        scheduleDay.schedule = subjects

        return scheduleDay
    }

    fun getCurrentSubject(schedule: Schedule): ScheduleSubject? {
        val calendarDate = CalendarDate(startDate = CalendarDate.TODAY_DATE)
        var scheduleSubject: ScheduleSubject? = null
        var isStop = false
        if (schedule.subjectNow == null) {
            schedule.schedules.map { day ->
                if (isStop) return@map
                day.schedule.map { subject ->
                    if (calendarDate.isCurrentSubject(subject.startLessonTime ?: "", subject.endLessonTime ?: "")) {
                        scheduleSubject = subject
                        isStop = true
                        return@map
                    }
                }
            }
        }

        return scheduleSubject
    }

    fun fillDatesInSchedule(schedule: Schedule, currentWeekNumber: Int) {
        val calendarDate = CalendarDate(startDate = CalendarDate.TODAY_DATE, currentWeekNumber)

        schedule.schedules.mapIndexed { index, day ->
            calendarDate.incDate(index)
            day.date = calendarDate.getDateStatus()
            day.dateUnixTime = calendarDate.getDateUnixTime()
            day.weekDayName = calendarDate.getWeekDayName()
            day.weekDayNumber = calendarDate.getWeekDayNumber()
            day.weekNumber = calendarDate.getWeekNumber()
        }
    }

    fun getActualSchedule(schedule: Schedule, fromCurrentDate: Boolean): Schedule {
        if (!fromCurrentDate) return schedule

        val calendarDate = CalendarDate(startDate = CalendarDate.TODAY_DATE)

        val scheduleDays = schedule.schedules.filterIndexed { index, day ->
            calendarDate.incDate(index)
            day.dateUnixTime >= calendarDate.getDateUnixTime()
        }

        schedule.schedules = scheduleDays as ArrayList<ScheduleDay>

        return schedule
    }

}


