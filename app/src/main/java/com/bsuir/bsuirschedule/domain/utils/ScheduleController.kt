package com.bsuir.bsuirschedule.domain.utils

import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import java.util.*
import kotlin.collections.ArrayList

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

    private fun getMillisTimeInSubjects(schedule: Schedule): Schedule {
        val newSchedule = schedule.copy()
        val calendarDate = CalendarDate(startDate = schedule.startDate)
        newSchedule.schedules.mapIndexed { index, day ->
            calendarDate.incDate(index)
            day.schedule.map { subject ->
                if (!subject.startLessonTime.isNullOrEmpty() && !subject.endLessonTime.isNullOrEmpty()) {
                    val startMillis = calendarDate.getTimeMillis(subject.startLessonTime)
                    val endMillis = calendarDate.getTimeMillis(subject.endLessonTime)
                    subject.startMillis = startMillis
                    subject.endMillis = endMillis
                }
            }
        }

        return newSchedule
    }

    private fun getActualSubject(schedule: Schedule): ScheduleSubject? {
        val dateUnixTime = Date().time
        val actualDays = schedule.schedules.filter { it.dateUnixTime >= dateUnixTime - 86400000 }

        var actualSubject: ScheduleSubject? = null
        for (actualDay in actualDays) {
            actualSubject = actualDay.schedule.find { subject ->
                (dateUnixTime < subject.startMillis) or
                        (dateUnixTime > subject.startMillis && dateUnixTime < subject.endMillis)
            }
            if (actualSubject != null) {
                break
            }
        }

        return actualSubject
    }

    private fun getMultipliedSchedule(schedule: Schedule, currentWeekNumber: Int): Schedule {
        val calendarDate = CalendarDate(startDate = schedule.startDate, currentWeekNumber) // FIXME
        var daysCounter = 0
        val scheduleDays = ArrayList<ScheduleDay>()

        while (!calendarDate.isEqualDate(schedule.endDate) && daysCounter < DAYS_LIMIT) {
            calendarDate.incDate(daysCounter)
            val weekNumber = calendarDate.getWeekNumber()
            val weekDayNumber = calendarDate.getWeekDayNumber()
            val weekNumberDays = schedule.schedules.filter { it.weekDayNumber == weekDayNumber }
            val weekNumberDaysCopy = weekNumberDays.map { it.copy() }
            if (weekNumberDaysCopy.isEmpty()) {
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
            weekNumberDaysCopy.map { scheduleDay ->
                val subjects = scheduleDay.schedule.filter { subject ->
                    weekNumber in (subject.weekNumber ?: ArrayList())
                } as ArrayList<ScheduleSubject>
                val subjectsCopy = subjects.map { it.copy() }
                scheduleDays.add(
                    ScheduleDay(
                        date = calendarDate.getDateStatus(),
                        dateUnixTime = calendarDate.getDateUnixTime(),
                        weekDayTitle = calendarDate.getWeekDayTitle(),
                        weekDayNumber = weekDayNumber,
                        weekNumber = weekNumber,
                        schedule = subjectsCopy as ArrayList<ScheduleSubject>
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

    private fun getSubgroupsList(schedule: ArrayList<ScheduleDay>): List<Int> {
        val amount = ArrayList<Int>()

        schedule.forEach { day ->
            day.schedule.forEach { subject ->
                amount.add(subject.numSubgroup ?: 0)
            }
        }

        return amount.toSet().toList().sorted()
    }

    // This schedule will be saved in DB
    fun getBasicSchedule(groupSchedule: GroupSchedule, currentWeekNumber: Int): Schedule {
        val schedule = getNormalSchedule(groupSchedule)
        schedule.subgroups = getSubgroupsList(schedule.schedules)
        val scheduleMultiplied = getMultipliedSchedule(schedule, currentWeekNumber)

        return getMillisTimeInSubjects(scheduleMultiplied)
    }

    // This schedule will be shown on UI
    fun getRegularSchedule(schedule: Schedule, currentWeekNumber: Int): Schedule {
        fillDatesInSchedule(schedule, currentWeekNumber)
        val filteredSchedule = filterActualSchedule(schedule)
        filteredSchedule.schedules = filterBySubgroup(filteredSchedule.schedules, filteredSchedule.selectedSubgroup)
        filteredSchedule.schedules = getSubjectsBreakTime(filteredSchedule.schedules)
        filteredSchedule.subjectNow = getActualSubject(filteredSchedule)

        return filteredSchedule
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

    private fun filterActualSchedule(schedule: Schedule, fromCurrentDate: Boolean = true): Schedule {
        if (!fromCurrentDate) return schedule
        val newSchedule = schedule.copy()

        val calendarDate = if (fromCurrentDate) {
            CalendarDate(startDate = CalendarDate.TODAY_DATE)
        } else {
            CalendarDate(startDate = newSchedule.startDate)
        }

        val scheduleDays = newSchedule.schedules.filterIndexed { index, day ->
            calendarDate.incDate(index)
            day.dateUnixTime >= calendarDate.getDateUnixTime()
        }

        newSchedule.schedules = scheduleDays as ArrayList<ScheduleDay>

        return newSchedule
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


