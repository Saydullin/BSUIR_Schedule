package com.example.bsuirschedule.domain.utils

import android.util.Log
import com.example.bsuirschedule.domain.models.*

class ScheduleManager {

    companion object {
        const val LIMIT_DAYS = 60
    }

    // Model schedules as list (instead of monday, tuesday, ...)
    fun getScheduleModel(groupSchedule: GroupSchedule): Schedule {

        if (groupSchedule.isNotSuitable()) {
            return Schedule.empty
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
                amount.add(subject.numSubgroup)
            }
        }

        return amount.toSet().toList()
    }

    fun getFullScheduleModel(schedule: Schedule): Schedule {
        if (schedule.schedules.isEmpty()) {
            return Schedule.empty
        }
        val currentWeek = 3
        var i = 0
        var weekAmount = currentWeek
        val calendarDate = CalendarDate(startDate = schedule.startDate, endDate = schedule.endDate)
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
            schedules = ArrayList()
        )

        for (y in 1..3) {
            // weeks loop
            for (weekNumber in 1..maxWeeks) {
                weekAmount++
                if (weekAmount >= maxWeeks) {
                    weekAmount = 1
                }
                for (dayNumber in beginOnDay..schedule.schedules.size) {
                    // Get ${week[dayNumber]} schedule days
                    val filteredDays = schedule.schedules.filter {scheduleDay -> scheduleDay.weekDayNumber == dayNumber }
                    for (filteredDay in filteredDays) {
                        // Get subjects in that day, which in that week
                        val filteredSchedule = filteredDay.schedule.filter { subject -> weekNumber in subject.weekNumber }
                        scheduleFull.schedules.add(
                            ScheduleDay(
                                date = calendarDate.getIncDate(i),
                                weekDayName = calendarDate.getWeekDayName(),
                                weekDayNumber = calendarDate.getWeekDayNumber(),
                                schedule = filteredSchedule as ArrayList<ScheduleSubject>,
                                lessonsAmount = filteredSchedule.size
                            ))
                        if (calendarDate.getWeekDayNumber() == 6) {
                            i++
                            scheduleFull.schedules.add(
                                ScheduleDay(
                                    date = calendarDate.getIncDate(i),
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
                val max = subject.weekNumber.maxOrNull() ?: 0
                if (max > weeksAmount) {
                    weeksAmount = max
                }
            }
        }

        return weeksAmount
    }

}