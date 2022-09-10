package com.bsuir.bsuirschedule.domain.utils

import android.util.Log
import com.bsuir.bsuirschedule.domain.models.*
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

        val scheduleDays = listOf(
            groupSchedule.schedules?.monday ?: ArrayList(),
            groupSchedule.schedules?.tuesday ?: ArrayList(),
            groupSchedule.schedules?.wednesday ?: ArrayList(),
            groupSchedule.schedules?.thursday ?: ArrayList(),
            groupSchedule.schedules?.friday ?: ArrayList(),
            groupSchedule.schedules?.saturday ?: ArrayList(),
        )

        scheduleDays.forEachIndexed { index, scheduleDay ->
            schedule.schedules.add(
                ScheduleDay(
                    date = calendarDate.getIncDate(index),
                    weekDayName = calendarDate.getWeekDayName(),
                    weekDayNumber = index + 1,
                    schedule = scheduleDay,
                    lessonsAmount = scheduleDay.size
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
                weekDayName = calendarDate.getWeekDayName(),
                weekDayNumber = calendarDate.getWeekDayNumber(),
                lessonsAmount = scheduleInDay.size,
                schedule = scheduleInDay
            )
            scheduleDays.add(scheduleDay)
            counter++
        }

        return getSubjectsBreakTime(scheduleDays)
    }

    fun mergeGroupsSubjects(groupSchedule: GroupSchedule, groupItems: ArrayList<Group>) {
        val days = listOf(
            groupSchedule.schedules?.monday ?: ArrayList(),
            groupSchedule.schedules?.tuesday ?: ArrayList(),
            groupSchedule.schedules?.wednesday ?: ArrayList(),
            groupSchedule.schedules?.thursday ?: ArrayList(),
            groupSchedule.schedules?.friday ?: ArrayList(),
            groupSchedule.schedules?.saturday ?: ArrayList()
        )

        days.map { day ->
            day.map { subject ->
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

    fun mergeGroupSubjects(schedule: Schedule, groupItems: ArrayList<Group>): Schedule {
        schedule.schedules.map { scheduleDay ->
            scheduleDay.schedule.map { scheduleSubject ->
                scheduleSubject.subjectGroups?.map { groupSubject ->
                    val groupItem = groupItems.find { it.name == groupSubject.name }
                    if (groupItem != null) {
                        scheduleSubject.groups?.add(groupItem)
                    }
                }
            }
        }

        return schedule
    }

    private fun getSubgroupsAmount(schedule: ArrayList<ScheduleDay>): List<Int> {
        val amount = ArrayList<Int>()

        schedule.forEach { day ->
            day.schedule.forEach { subject ->
                amount.add(subject.numSubgroup ?: 0)
            }
        }

        return amount.toSet().toList()
    }

    private fun getSubjectList(scheduleDays: ArrayList<ScheduleDay>): ArrayList<ScheduleSubject> {
        val subjectList = ArrayList<ScheduleSubject>()

        scheduleDays.map { day ->
            day.schedule.map { subject ->
                subject.dayNumber = day.weekDayNumber
            }
            subjectList.addAll(day.schedule)
        }

        subjectList.sortBy { it.startLessonTime }

        return subjectList
    }

    fun getFullSchedule(schedule: Schedule, currentWeek: Int, fromCurrentDay: Boolean = true): ArrayList<ScheduleDay> {
        if (schedule.schedules.isEmpty()) {
            return ArrayList()
        }

        val scheduleDays = ArrayList<ScheduleDay>()
        val endDate = schedule.endDate
        var dayCounter = 0
        val calendarDate = if (fromCurrentDay) {
            CalendarDate(startDate = CalendarDate.TODAY_DATE, weekNumber = currentWeek)
        } else {
            CalendarDate(startDate = schedule.startDate, weekNumber = currentWeek)
        }

        var currentDate = calendarDate.getFullDate(0)
        val scheduleSubjects = getSubjectList(schedule.schedules)
        Log.e("sady", "Subjects has ${scheduleSubjects.size} elements")
        val maxWeeks = getWeeksAmount(schedule.schedules)

        while (currentDate != endDate && dayCounter < LIMIT_DAYS || dayCounter == 0) {
            val subjectsDay = ArrayList<ScheduleSubject>()
            currentDate = calendarDate.getFullDate(dayCounter)
            dayCounter++
            scheduleSubjects.map { subject ->
                if (subject.startLessonDate.isNullOrEmpty() || subject.endLessonDate.isNullOrEmpty()) return@map
                if (calendarDate.isMatchDate(subject.startLessonDate, subject.endLessonDate) &&
                        subject.weekNumber?.contains(calendarDate.getWeekNumber()) == true &&
                        subject.dayNumber == calendarDate.getWeekDayNumber()) {
                    subjectsDay.add(subject)
                }
            }
            scheduleDays.add(ScheduleDay(
                date = calendarDate.getDate(),
                weekDayName = calendarDate.getWeekDayName(),
                weekDayNumber = calendarDate.getWeekNumber(),
                lessonsAmount = subjectsDay.size,
                schedule = subjectsDay
            ))
        }

//        while (currentDate != endDate && dayCounter < LIMIT_DAYS || dayCounter == 0) {
//            for (weekIndex in currentWeek..maxWeeks + maxWeeks) {
//                week = if (week >= maxWeeks) 1 else week++
//                schedule.schedules.map { day ->
//                    currentDate = calendarDate.getFullDate(dayCounter)
//                    dayCounter++
//                    val subjectInDay = ArrayList<ScheduleSubject>()
//                    if (calendarDate.getWeekDayNumber() != 0) {
//                        day.schedule.map { subject ->
//                            if (subject.weekNumber?.contains(week) == true) {
//                                subjectInDay.add(subject)
//                            }
//                        }
//                    }
//                    calendarDate.getIncDate(dayCounter)
//                    scheduleDays.add(ScheduleDay(
//                        date = calendarDate.getDateStatus(),
//                        weekDayName = calendarDate.getWeekDayName(),
//                        weekDayNumber = calendarDate.getWeekDayNumber(),
//                        lessonsAmount = subjectInDay.size,
//                        schedule = subjectInDay
//                    ))
//                }
//            }
//        }

        return scheduleDays
    }

    fun getFullScheduleModel(schedule: Schedule, weekNumber: Int, fromCurrentDay: Boolean = true): Schedule {
        if (schedule.schedules.isEmpty()) {
            return Schedule.empty
        }
        var i = 0
        var week = weekNumber - 1
        val calendarDate = if (fromCurrentDay) {
            CalendarDate(startDate = CalendarDate.TODAY_DATE)
        } else {
            CalendarDate(startDate = schedule.startDate)
        }
        var beginOnDay = calendarDate.getWeekDayNumber()
        val maxWeeks = getWeeksAmount(schedule.schedules)
        val subgroups = getSubgroupsAmount(schedule.schedules)

        val scheduleFull = Schedule(
            id = schedule.id,
            startDate = schedule.startDate,
            endDate = schedule.endDate,
            startExamsDate = schedule.startExamsDate,
            endExamsDate = schedule.endExamsDate,
            group = schedule.group,
            employee = schedule.employee,
            isGroup = schedule.group.id != -1,
            subgroups = subgroups,
            exams = schedule.exams,
            examsSchedule = schedule.examsSchedule,
            lastUpdateTime = schedule.lastUpdateTime,
            schedules = ArrayList()
        )

        for (y in 1..3) {
            // weeks loop
            for (weekIndex in 1..maxWeeks + week) {
                week++
                if (week >= maxWeeks) {
                    week = 1
                }
                for (dayNumber in beginOnDay..schedule.schedules.size) {
                    // Get ${week[dayNumber]} schedule days
                    val filteredDays = schedule.schedules.filter {scheduleDay -> scheduleDay.weekDayNumber == dayNumber }
                    for (filteredDay in filteredDays) {
                        // Get subjects in that day, which in that week
                        val filteredSchedule = filteredDay.schedule.filter { subject -> week in (subject.weekNumber ?: ArrayList()) }
                        calendarDate.getIncDate(i)
                        scheduleFull.schedules.add(
                            ScheduleDay(
                                date = calendarDate.getDateStatus(),
                                weekDayName = calendarDate.getWeekDayName(),
                                weekDayNumber = calendarDate.getWeekDayNumber(),
                                schedule = filteredSchedule as ArrayList<ScheduleSubject>,
                                lessonsAmount = filteredSchedule.size
                            ))
                        if (calendarDate.getWeekDayNumber() == 6) {
                            i++
                            calendarDate.getIncDate(i)
                            scheduleFull.schedules.add(
                                ScheduleDay(
                                    date = calendarDate.getDateStatus(),
                                    weekDayName = calendarDate.getWeekDayName(),
                                    weekDayNumber = calendarDate.getWeekDayNumber(),
                                    schedule = ArrayList(),
                                    lessonsAmount = 0
                                ))
                        }
                        i++
                    }
                }
                beginOnDay = 1
            }
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