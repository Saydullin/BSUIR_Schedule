package com.bsuir.bsuirschedule.domain.utils

import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import java.util.*
import kotlin.collections.ArrayList

class ScheduleController {

    companion object {
        const val DAYS_LIMIT = 200
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

        if (schedule.isGroup()) {
            schedule.id = groupSchedule.group?.id ?: -1
        } else {
            schedule.id = groupSchedule.employee?.id ?: -1
        }

        schedule.schedules = weekDays

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
        val calendarDate = CalendarDate(startDate = schedule.startDate, currentWeekNumber)
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
                        date = calendarDate.getDateStatus() + ", " + weekDayNumber,
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
                        date = calendarDate.getDateStatus() + ", " + weekDayNumber,
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

    private fun getSubgroupsList(schedule: Schedule): List<Int> {
        val amount = ArrayList<Int>()

        amount.add(0)

        if (!schedule.isNotExistSchedule()) {
            schedule.schedules.forEach { day ->
                day.schedule.forEach { subject ->
                    amount.add(subject.numSubgroup ?: 0)
                }
            }
        } else if (!schedule.isExamsNotExist()) {
            schedule.exams.forEach { subject ->
                amount.add(subject.numSubgroup ?: 0)
            }
        }

        return amount.toSet().toList().sorted()
    }

    private fun setDatesFromBeginSchedule(schedule: Schedule, currentWeekNumber: Int): Schedule {
        val newSchedule = schedule.copy()
        val calendarDate = CalendarDate(startDate = newSchedule.startDate, currentWeekNumber)

        newSchedule.schedules.mapIndexed { index, day ->
            calendarDate.incDate(index)
            day.date = calendarDate.getDate()
            day.dateUnixTime = calendarDate.getDateUnixTime()
            day.weekDayTitle = calendarDate.getWeekDayTitle()
            day.weekDayNumber = calendarDate.getWeekDayNumber()
            day.weekNumber = calendarDate.getWeekNumber()
        }

        return newSchedule
    }

    // This schedule will be saved in DB
    fun getBasicSchedule(groupSchedule: GroupSchedule, currentWeekNumber: Int): Schedule {
        val schedule = getNormalSchedule(groupSchedule)

        if (!schedule.isExamsNotExist()) {
            schedule.examsSchedule = getDaysFromSubjects(
                schedule.exams,
                schedule.startExamsDate,
                schedule.endExamsDate
            )
        }

        schedule.subgroups = getSubgroupsList(schedule)

        if (schedule.isNotExistSchedule()) {
            return schedule
        }

        val scheduleMultiplied = getMultipliedSchedule(schedule, currentWeekNumber)
        val daysWithDatesSchedule = setDatesFromBeginSchedule(scheduleMultiplied, currentWeekNumber)

        setSubjectsPrediction(daysWithDatesSchedule)

        return getMillisTimeInSubjects(daysWithDatesSchedule)
    }

    private fun getPagingLimit(schedule: ArrayList<ScheduleDay>, page: Int, pageSize: Int): ArrayList<ScheduleDay> {
        val limitedScheduleDays = ArrayList<ScheduleDay>()

        val loopUntil = if (schedule.size < pageSize) schedule.size else pageSize

        for (i in 0 until loopUntil) {
            limitedScheduleDays.add(schedule[i])
        }

        return limitedScheduleDays
    }

    // This schedule will be shown on UI
    fun getRegularSchedule(schedule: Schedule, page: Int, pageSize: Int): Schedule {
        val regularSchedule = schedule.copy()

        if (!schedule.isExamsNotExist()) {
            if (!schedule.settings.exams.isShowPastDays) {
                regularSchedule.examsSchedule = filterActualSchedule(regularSchedule.examsSchedule)
            } else {
                regularSchedule.examsSchedule = filterActualSchedule(
                    regularSchedule.examsSchedule,
                    30
                )
            }
            regularSchedule.examsSchedule = setActualDateStatuses(
                regularSchedule.examsSchedule,
                regularSchedule.settings.exams.isShowPastDays
            )
            regularSchedule.examsSchedule = filterBySubgroup(
                regularSchedule.examsSchedule,
                regularSchedule.settings.subgroup.selectedNum
            )
            regularSchedule.examsSchedule = getSubjectsBreakTime(regularSchedule.examsSchedule)
            regularSchedule.examsSchedule = getScheduleBySettings(
                regularSchedule.examsSchedule,
                regularSchedule.settings.exams.isShowEmptyDays
            )
        } else {
            regularSchedule.examsSchedule = ArrayList()
        }

        if (!schedule.isNotExistSchedule()) {
            if (!schedule.settings.schedule.isShowPastDays) {
                regularSchedule.schedules = filterActualSchedule(regularSchedule.schedules)
            } else {
                regularSchedule.schedules = filterActualSchedule(regularSchedule.schedules, regularSchedule.settings.schedule.pastDaysNumber)
            }

//            regularSchedule.schedules = getPagingLimit(regularSchedule.schedules, page, 3)
            regularSchedule.schedules = setActualDateStatuses(regularSchedule.schedules, schedule.settings.schedule.isShowPastDays)
            regularSchedule.schedules = filterBySubgroup(
                regularSchedule.schedules,
                regularSchedule.settings.subgroup.selectedNum
            )
            regularSchedule.schedules = getSubjectsBreakTime(regularSchedule.schedules)
            regularSchedule.subjectNow = getActualSubject(regularSchedule)
            regularSchedule.schedules = getScheduleBySettings(
                regularSchedule.schedules,
                regularSchedule.settings.schedule.isShowEmptyDays
            )
        } else {
            regularSchedule.schedules = ArrayList()
        }

        return regularSchedule
    }

    private fun setSubjectsPrediction(schedule: Schedule) {
        schedule.schedules.mapIndexed { index, day ->
            day.schedule.map { subject ->
                var i = 0
                for (dayIndex in index + 1 until schedule.schedules.size) {
                    i++
                    val scheduleDay = schedule.schedules[dayIndex]
                    val foundSubject = scheduleDay.schedule.find {
                        it.subject == subject.subject && it.lessonTypeAbbrev == subject.lessonTypeAbbrev &&
                                ((it.numSubgroup ?: 0) == 0
                                        ||
                                        (it.numSubgroup ?: 0) == schedule.settings.subgroup.selectedNum)
                    }

                    if (foundSubject != null) {
                        subject.nextTimeDaysLeft = i
                        subject.nextTimeSubject = subject
                        break
                    }
                }
            }
        }
    }

    private fun getScheduleBySettings(schedule: ArrayList<ScheduleDay>, isShowEmptyDays: Boolean): ArrayList<ScheduleDay> {
        val scheduleDays = ArrayList<ScheduleDay>()
        scheduleDays.addAll(schedule)

        if (!isShowEmptyDays) {
            schedule.map { day ->
                if (day.schedule.size == 0) {
                    scheduleDays.remove(day)
                }
            }
        }

        return scheduleDays
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

    // Set in ScheduleDay: "Yesterday", "Today" and "Tomorrow" values
    private fun setActualDateStatuses(schedule: ArrayList<ScheduleDay>, isShowPastDays: Boolean): ArrayList<ScheduleDay> {
        val calendarDate = CalendarDate(startDate = CalendarDate.TODAY_DATE)

        val todayStartIndex = schedule.indexOfFirst { it.dateUnixTime == calendarDate.getDateUnixTime() }
        if (todayStartIndex == -1) return schedule

        if (isShowPastDays && todayStartIndex > 0) {
            calendarDate.minusDays(1)
            schedule[todayStartIndex - 1].date = calendarDate.getDateStatus()
        }

        for ((dateCounter, i) in (todayStartIndex..todayStartIndex + 2).withIndex()) {
            if (i >= schedule.size) break
            calendarDate.incDate(dateCounter)
            schedule[i].date = calendarDate.getDateStatus()
        }

        return schedule
    }

    private fun filterActualSchedule(schedule: ArrayList<ScheduleDay>, preDays: Int = 0): ArrayList<ScheduleDay> {
        val calendarDate = CalendarDate(startDate = CalendarDate.TODAY_DATE)
        if (preDays > 0) {
            calendarDate.minusDays(preDays)
        }

        val scheduleDays = schedule.filter { day ->
            day.dateUnixTime >= calendarDate.getDateUnixTime()
        }

        return scheduleDays as ArrayList<ScheduleDay>
    }

    private fun getDaysFromSubjects(
        subjects: ArrayList<ScheduleSubject>,
        startDate: String,
        endDate: String,
        currentWeek: Int = 1
    ): ArrayList<ScheduleDay> {
        val calendarDate = CalendarDate(startDate = startDate, weekNumber = currentWeek)
        val scheduleDays = ArrayList<ScheduleDay>()
        var daysCounter = 0

        while (!calendarDate.isEqualDate(endDate) && daysCounter < DAYS_LIMIT) {
            val subjectsDay = subjects.filter { subject ->
                subject.dateLesson == calendarDate.getFullDate(daysCounter)
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


