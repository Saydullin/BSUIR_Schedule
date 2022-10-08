package com.bsuir.bsuirschedule.domain.utils

import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject

class ScheduleController {

    companion object {
        const val DAYS_LIMIT = 100
    }

    private fun getNormalSchedule(groupSchedule: GroupSchedule): Schedule {
        val weekDays = arrayListOf(
            initScheduleDay(groupSchedule.schedules?.monday ?: ArrayList(), 1),
            initScheduleDay(groupSchedule.schedules?.tuesday ?: ArrayList(), 2),
            initScheduleDay(groupSchedule.schedules?.wednesday ?: ArrayList(), 3),
            initScheduleDay(groupSchedule.schedules?.thursday ?: ArrayList(), 4),
            initScheduleDay(groupSchedule.schedules?.friday ?: ArrayList(), 5),
            initScheduleDay(groupSchedule.schedules?.saturday ?: ArrayList(), 6),
            initScheduleDay(groupSchedule.schedules?.sunday ?: ArrayList(), 0),
        )

        val schedule = groupSchedule.toSchedule()
        schedule.schedules = weekDays

        schedule.examsSchedule = getDaysFromSubjects(
            schedule.exams,
            schedule.startExamsDate,
            schedule.endExamsDate
        )

        return schedule
    }

    private fun initScheduleDay(subjects: ArrayList<ScheduleSubject>, dayNumber: Int): ScheduleDay {
        val scheduleDay = ScheduleDay.empty.copy()
        scheduleDay.weekDayNumber = dayNumber
        scheduleDay.schedule = subjects

        return scheduleDay
    }

    private fun getCurrentSubject(schedule: Schedule, currentWeek: Int): ScheduleSubject? {
        val calendarDate = CalendarDate(startDate = CalendarDate.TODAY_DATE)
        var scheduleSubject: ScheduleSubject? = null
        var isStop = false
        schedule.schedules.map { day ->
            if (isStop) return@map
            day.schedule.map { subject ->
                if (subject.weekNumber?.contains(currentWeek) == true) {
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

    private fun getMultipliedSchedule(schedule: Schedule, currentWeekNumber: Int): Schedule {
        val calendarDate = CalendarDate(startDate = CalendarDate.TODAY_DATE, currentWeekNumber)
        var daysCounter = 0
        val scheduleDays = ArrayList<ScheduleDay>()

        while (!calendarDate.isEqualDate(schedule.endDate) && daysCounter < DAYS_LIMIT) {
            calendarDate.incDate(daysCounter)
            val weekNumber = calendarDate.getWeekNumber()
            val weekDayNumber = calendarDate.getWeekDayNumber()
            val weekNumberDays = schedule.schedules.filter { it.weekDayNumber == weekDayNumber }
            if (weekNumberDays.isEmpty()) {
                scheduleDays.add(
                    ScheduleDay(
                        date = calendarDate.getDateStatus(),
                        dateUnixTime = calendarDate.getDateUnixTime(),
                        weekDayTitle = calendarDate.getWeekDayTitle(),
                        weekDayNumber = weekDayNumber,
                        weekNumber = weekNumber,
                        schedule = ArrayList()
                    ))
            }
            weekNumberDays.map { scheduleDay ->
                val subjects = scheduleDay.schedule.filter { subject ->
                    weekNumber in (subject.weekNumber ?: ArrayList())
                } as ArrayList<ScheduleSubject>
                scheduleDays.add(
                    ScheduleDay(
                        date = calendarDate.getDateStatus(),
                        dateUnixTime = calendarDate.getDateUnixTime(),
                        weekDayTitle = calendarDate.getWeekDayTitle(),
                        weekDayNumber = weekDayNumber,
                        weekNumber = weekNumber,
                        schedule = subjects
                    ))
            }
            daysCounter++
        }
        schedule.schedules = scheduleDays

        return schedule
    }

    private fun getSubjectsBreakTime(scheduleDays: ArrayList<ScheduleDay>): ArrayList<ScheduleDay> {
        val subjectsBreakTime = ArrayList<ScheduleDay>()
        val calendarDate = CalendarDate()

        for (scheduleItem in scheduleDays) {
            val scheduleDay = scheduleItem.copy()
            val subjectsList = ArrayList<ScheduleSubject>()
            for (subjectIndex in 0 until scheduleItem.schedule.size) {
                val subject = scheduleItem.schedule[subjectIndex].copy()
                if (subjectIndex != 0) {
                    val currSubject = scheduleItem.schedule[subjectIndex]
                    val prevSubject = scheduleItem.schedule[subjectIndex-1]
                    subject.breakTime = calendarDate.getSubjectBreakTime(
                        currSubject.startLessonTime,
                        prevSubject.endLessonTime
                    )
                }
                subjectsList.add(subject)
            }
            scheduleDay.schedule = subjectsList
            subjectsBreakTime.add(scheduleDay)
        }

        return subjectsBreakTime
    }

    fun getBasicSchedule(groupSchedule: GroupSchedule, currentWeekNumber: Int): Schedule {
        val schedule = getNormalSchedule(groupSchedule)
        val scheduleMultiplied = getMultipliedSchedule(schedule, currentWeekNumber)

        return scheduleMultiplied
    }

    fun getRegularSchedule(schedule: Schedule, currentWeekNumber: Int): Schedule {
        fillDatesInSchedule(schedule, currentWeekNumber)
        getActualSchedule(schedule)
        schedule.schedules = filterBySubgroup(schedule.schedules, schedule.selectedSubgroup)
        schedule.schedules = getSubjectsBreakTime(schedule.schedules)
        schedule.subjectNow = getCurrentSubject(schedule, currentWeekNumber)

        return schedule
    }

    private fun filterBySubgroup(schedule: ArrayList<ScheduleDay>, subgroup: Int): ArrayList<ScheduleDay> {
        val scheduleList = ArrayList<ScheduleDay>()
        if (subgroup == 0) return schedule

        schedule.map { day ->
            val subjects = day.schedule.filter { it.numSubgroup == 0 || it.numSubgroup == subgroup }
            day.schedule = subjects as ArrayList<ScheduleSubject>
            scheduleList.add(day)
        }

        return scheduleList
    }

    private fun fillDatesInSchedule(schedule: Schedule, currentWeekNumber: Int) {
        val calendarDate = CalendarDate(startDate = CalendarDate.TODAY_DATE, currentWeekNumber)

        schedule.schedules.mapIndexed { index, day ->
            calendarDate.incDate(index)
            day.date = calendarDate.getDateStatus()
            day.dateUnixTime = calendarDate.getDateUnixTime()
            day.weekDayTitle = calendarDate.getWeekDayTitle()
            day.weekDayNumber = calendarDate.getWeekDayNumber()
            day.weekNumber = calendarDate.getWeekNumber()
        }
    }

    private fun getActualSchedule(schedule: Schedule, fromCurrentDate: Boolean = true): Schedule {
        if (!fromCurrentDate) return schedule

        val calendarDate = CalendarDate(startDate = CalendarDate.TODAY_DATE)

        val scheduleDays = schedule.schedules.filterIndexed { index, day ->
            calendarDate.incDate(index)
            day.dateUnixTime >= calendarDate.getDateUnixTime()
        }

        schedule.schedules = scheduleDays as ArrayList<ScheduleDay>

        return schedule
    }

    private fun getDaysFromSubjects(
        subjects: ArrayList<ScheduleSubject>,
        startDate: String,
        endDate: String,
        currentWeek: Int = 1,
        fromCurrentDate: Boolean = true
    ): ArrayList<ScheduleDay> {
        val calendarDate = if (fromCurrentDate) {
            CalendarDate(startDate = CalendarDate.TODAY_DATE, weekNumber = currentWeek)
        } else {
            CalendarDate(startDate = startDate, weekNumber = currentWeek)
        }
        val scheduleDays = ArrayList<ScheduleDay>()
        var daysCounter = 0

        while (!calendarDate.isEqualDate(endDate) && daysCounter < DAYS_LIMIT) {
            val subjectsDay = subjects.filter { subject ->
                subject.dateLesson == calendarDate.getIncDate(daysCounter)
            }
            scheduleDays.add(ScheduleDay(
                date = calendarDate.getDate(),
                dateUnixTime = calendarDate.getDateUnixTime(),
                weekDayTitle = calendarDate.getWeekDayTitle(),
                weekDayNumber = calendarDate.getWeekDayNumber(),
                weekNumber = calendarDate.getWeekNumber(),
                schedule = subjectsDay as ArrayList<ScheduleSubject>
            ))
            daysCounter++
        }

        return scheduleDays
    }
}


