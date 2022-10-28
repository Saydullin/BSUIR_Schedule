package com.bsuir.bsuirschedule.domain.utils

import com.bsuir.bsuirschedule.domain.models.*
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import kotlin.collections.ArrayList

class ScheduleManager {

    companion object {
        const val LIMIT_DAYS = 100
    }

    // Model schedules as list (instead of monday, tuesday, ...)
    fun getScheduleModel(groupSchedule: GroupSchedule): Schedule {

        if (groupSchedule.isNotSuitable()) {
            return groupSchedule.toSchedule()
        }

        val calendarDate = CalendarDate(startDate = groupSchedule.startDate!!)
        val schedule = groupSchedule.toSchedule()
        schedule.schedules = ArrayList()

        // Monday, tuesday, wednesday, thursday ...
        val scheduleDays = groupSchedule.schedules?.getList() ?: ArrayList()
        var counter = 0

        scheduleDays.forEach { scheduleDay ->
            counter++
            schedule.schedules.add(
                ScheduleDay(
                    date = calendarDate.getIncDate(counter),
                    dateUnixTime = calendarDate.getDateUnixTime(),
                    weekDayTitle = calendarDate.getWeekDayTitle(),
                    weekDayNumber = counter,
                    weekNumber = calendarDate.getWeekNumber(),
                    schedule = scheduleDay,
                )
            )
        }

        return schedule
    }

    fun getSubjectDays(subjects: ArrayList<ScheduleSubject>, startDate: String, endDate: String): ArrayList<ScheduleDay> {
        val scheduleDays = ArrayList<ScheduleDay>()
        val calendarDate = CalendarDate(startDate = startDate)
        var currentDate = calendarDate.getFullDate(0)
        var counter = 0

        while(currentDate != endDate && counter < LIMIT_DAYS || counter == 0) {
            val scheduleInDay = ArrayList<ScheduleSubject>()
            currentDate = calendarDate.getFullDate(counter)
            subjects.map { subject ->
                if (subject.dateLesson == currentDate) {
                    scheduleInDay.add(subject)
                }
            }
            scheduleInDay.sortBy { it.startLessonTime }
            val scheduleDay = ScheduleDay(
                date = calendarDate.getDate(),
                dateUnixTime = calendarDate.getDateUnixTime(),
                weekDayTitle = calendarDate.getWeekDayTitle(),
                weekDayNumber = calendarDate.getWeekDayNumber(),
                weekNumber = calendarDate.getWeekNumber(),
                schedule = scheduleInDay
            )
            scheduleDays.add(scheduleDay)
            counter++
        }

        return getSubjectsBreakTime(scheduleDays)
    }

    fun mergeGroupsSubjects(schedule: Schedule, groupItems: ArrayList<Group>) {
        schedule.schedules.map { day ->
            day.schedule.map { subject ->
                val groups = ArrayList<Group>()
                subject.subjectGroups?.map { subjectGroup ->
                    val groupItem = groupItems.find { it.name == subjectGroup.name }
                    if (groupItem != null) {
                        groups.add(groupItem)
                    }
                }
                subject.groups = groups
            }
        }
    }

    fun filterBySubgroup(schedule: ArrayList<ScheduleDay>, subgroup: Int): ArrayList<ScheduleDay> {
        val scheduleList = ArrayList<ScheduleDay>()
        if (subgroup == 0) return schedule

        schedule.map { day ->
            val subjects = day.schedule.filter { it.numSubgroup == 0 || it.numSubgroup == subgroup }
            day.schedule = subjects as ArrayList<ScheduleSubject>
            scheduleList.add(day)
        }

        return scheduleList
    }

    fun getFullSchedule(schedule: Schedule, week: Int, fromCurrentDay: Boolean = true): ArrayList<ScheduleDay> {
        if (schedule.schedules.isEmpty()) {
            return ArrayList()
        }
        val scheduleList = ArrayList<ScheduleDay>()
        val calendarDate = if (fromCurrentDay) {
            CalendarDate(startDate = CalendarDate.TODAY_DATE, week)
        } else {
            CalendarDate(startDate = schedule.startDate, week)
        }
        var i = 0
        var dayNumber = 1
        var weekNumber = week

        while (!calendarDate.isEqualDate(schedule.endDate) && i < LIMIT_DAYS) {
            val filteredDaysByDay = schedule.schedules.filter { scheduleDay ->
                scheduleDay.weekDayNumber == dayNumber
            }
            filteredDaysByDay.map { scheduleDay ->
                scheduleDay.schedule = scheduleDay.schedule.filter { (it.weekNumber ?: ArrayList()).contains(weekNumber)
                } as ArrayList<ScheduleSubject>
            }

            calendarDate.getIncDate(i)
            if (filteredDaysByDay.isEmpty()) {
                scheduleList.add(
                    ScheduleDay(
                        date = calendarDate.getDateStatus(),
                        dateUnixTime = calendarDate.getDateUnixTime(),
                        weekDayTitle = calendarDate.getWeekDayTitle(),
                        weekDayNumber = calendarDate.getWeekDayNumber(),
                        weekNumber = calendarDate.getWeekNumber(),
                        schedule = ArrayList(),
                    ))
            }
            filteredDaysByDay.map { day ->
                scheduleList.add(
                    ScheduleDay(
                        date = calendarDate.getDateStatus(),
                        dateUnixTime = calendarDate.getDateUnixTime(),
                        weekDayTitle = calendarDate.getWeekDayTitle(),
                        weekDayNumber = calendarDate.getWeekDayNumber(),
                        weekNumber = calendarDate.getWeekNumber(),
                        schedule = day.schedule,
                    ))
            }

            dayNumber++
            if (dayNumber == 8) {
                dayNumber = 1
            }
            if (calendarDate.isSunday()) {
                weekNumber++
            }
            i++
        }

        return scheduleList
    }

    fun setCurrentSubject(schedule: Schedule) {
        val calendarDate = CalendarDate(startDate = CalendarDate.TODAY_DATE)
        var isStop = false
        if (schedule.subjectNow == null) {
            schedule.schedules.map { day ->
                if (isStop) return@map
                day.schedule.map { subject ->
                    if (calendarDate.isCurrentSubject(subject.startLessonTime ?: "", subject.endLessonTime ?: "")) {
                        schedule.subjectNow = subject
                        isStop = true
                        return@map
                    }
                }
            }
        }
    }

    fun getFullScheduleModel(schedule: Schedule, weekNumber: Int, fromCurrentDay: Boolean = true): Schedule {
        if (schedule.schedules.isEmpty()) {
            return Schedule.empty
        }

        var i = 0
        var week = weekNumber - 1
        var daysCounter = 0
        var isDone = false
        val calendarDate = if (fromCurrentDay) {
            CalendarDate(startDate = CalendarDate.TODAY_DATE, weekNumber)
        } else {
            CalendarDate(startDate = schedule.startDate, weekNumber)
        }
        var beginOnDay = calendarDate.getWeekDayNumber()

        val maxWeeks = getWeeksAmount(schedule.schedules)

        // FIXME Copy Object
        val scheduleFull = Schedule(
            id = schedule.id,
            startDate = schedule.startDate,
            endDate = schedule.endDate,
            startExamsDate = schedule.startExamsDate,
            endExamsDate = schedule.endExamsDate,
            group = schedule.group,
            employee = schedule.employee,
            isGroup = schedule.group.id != -1,
            subgroups = schedule.subgroups,
            exams = schedule.exams,
            examsSchedule = schedule.examsSchedule,
            subjectNow = null,
            lastUpdateTime = schedule.lastUpdateTime,
            schedules = ArrayList(),
            settings = ScheduleSettings.empty
        )

        while (!isDone) {
            daysCounter++
            week++
            if (week >= maxWeeks) {
                week = 1
            }
            for (dayNumber in beginOnDay..7) {
                if (calendarDate.isEqualDate(schedule.endDate) || daysCounter >= LIMIT_DAYS) {
                    isDone = true
                    break
                }
                val filteredDays = schedule.schedules.filter { scheduleDay -> scheduleDay.weekDayNumber == dayNumber }
                if (filteredDays.isEmpty()) {
                    calendarDate.getIncDate(i)
                    scheduleFull.schedules.add(
                        ScheduleDay(
                            date = calendarDate.getDateStatus(),
                            dateUnixTime = calendarDate.getDateUnixTime(),
                            weekDayTitle = calendarDate.getWeekDayTitle(),
                            weekDayNumber = calendarDate.getWeekDayNumber(),
                            weekNumber = calendarDate.getWeekNumber(),
                            schedule = ArrayList(),
                        ))
                    i++
                }
                calendarDate.getIncDate(i)
                val weekNum = calendarDate.getWeekNumber()
                val filteredSubjects = filteredDays[0].schedule.filter { subject -> weekNum in (subject.weekNumber ?: ArrayList()) }
                scheduleFull.schedules.add(
                    ScheduleDay(
                        date = calendarDate.getDateStatus(),
                        dateUnixTime = calendarDate.getDateUnixTime(),
                        weekDayTitle = calendarDate.getWeekDayTitle(),
                        weekDayNumber = calendarDate.getWeekDayNumber(),
                        weekNumber = calendarDate.getWeekNumber(),
                        schedule = filteredSubjects as ArrayList<ScheduleSubject>,
                    ))
                i++
            }
            beginOnDay = 1
        }

        return scheduleFull
    }

    fun getSubjectsBreakTime(scheduleDays: ArrayList<ScheduleDay>): ArrayList<ScheduleDay> {
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

    private fun getWeeksAmount(scheduleDays: ArrayList<ScheduleDay>): Int {
        var weeksAmount = 0
        for (scheduleDay in scheduleDays) {
            for (subject in scheduleDay.schedule) {
                val max = subject.weekNumber?.maxOrNull() ?: 0
                if (max > weeksAmount) {
                    weeksAmount = max
                }
            }
        }

        return weeksAmount
    }

}