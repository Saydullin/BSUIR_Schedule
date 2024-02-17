package com.bsuir.bsuirschedule.domain.utils

import com.bsuir.bsuirschedule.domain.models.*
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScheduleController {

    companion object {
        const val DAYS_LIMIT = 100
    }

    private fun getNormalSchedule(groupSchedule: GroupSchedule): Schedule {
        val weekDays = arrayListOf(
            initScheduleDay(groupSchedule.schedules?.monday ?: ArrayList(), 1, Calendar.MONDAY),
            initScheduleDay(groupSchedule.schedules?.tuesday ?: ArrayList(), 2, Calendar.TUESDAY),
            initScheduleDay(groupSchedule.schedules?.wednesday ?: ArrayList(), 3, Calendar.WEDNESDAY),
            initScheduleDay(groupSchedule.schedules?.thursday ?: ArrayList(), 4, Calendar.THURSDAY),
            initScheduleDay(groupSchedule.schedules?.friday ?: ArrayList(), 5, Calendar.FRIDAY),
            initScheduleDay(groupSchedule.schedules?.saturday ?: ArrayList(), 6, Calendar.SATURDAY),
            initScheduleDay(groupSchedule.schedules?.sunday ?: ArrayList(), 0, Calendar.SUNDAY),
        )

        val schedule = groupSchedule.toSchedule()

        if (schedule.isGroup()) {
            schedule.id = groupSchedule.group?.id ?: -1
        } else {
            schedule.id = groupSchedule.employee?.id ?: -1
        }

        schedule.schedules = weekDays

        val subjectTypes = listOf(
            ScheduleSubject.LESSON_TYPE_LECTURE,
            ScheduleSubject.LESSON_TYPE_PRACTISE,
            ScheduleSubject.LESSON_TYPE_LABORATORY,
        )
        filterSubjectsByType(schedule.schedules, subjectTypes)

        return schedule
    }

    private fun filterSubjectsByType(scheduleDaysList: ArrayList<ScheduleDay>, typesList: List<String>) {
        scheduleDaysList.map { scheduleDay ->
            scheduleDay.schedule = scheduleDay.schedule.filter { typesList.contains(it.lessonTypeAbbrev) } as ArrayList<ScheduleSubject>
        }
    }

    private fun setSubjectIds(schedule: Schedule) {
        var idCounter = 0
        schedule.schedules.map { day ->
            day.schedule.map { subject ->
                subject.id = idCounter
                idCounter++
            }
        }
    }

    fun mergeGroupsSubjects(schedules: ArrayList<ScheduleDay>, groupItems: ArrayList<Group>) {
        schedules.map { day ->
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

    private fun initScheduleDay(subjects: ArrayList<ScheduleSubject>, dayNumber: Int, calendarDayOfWeek: Int): ScheduleDay {
        val calendar = Calendar.getInstance(Locale("ru", "BY"))
        calendar.set(Calendar.DAY_OF_WEEK, calendarDayOfWeek)
        val output = SimpleDateFormat("EEEE")

        return ScheduleDay(
            id = -1,
            date = "",
            dateInMillis = 0,
            weekDayTitle = output.format(calendar.time),
            weekDayNumber = dayNumber,
            weekNumber = -1,
            schedule = subjects
        )
    }

    fun getMillisTimeInOriginalScheduleSubjects(schedule: Schedule): ArrayList<ScheduleDay> {
        val newSchedule = schedule.copy()
        val calendarDate = CalendarDate(startDate = schedule.startDate)
        newSchedule.originalSchedule.mapIndexed { index, day ->
            calendarDate.incDate(index)
            day.schedule.map { subject ->
                if (!subject.startLessonTime.isNullOrEmpty() && !subject.endLessonTime.isNullOrEmpty()) {
                    val startMillis = calendarDate.getTimeInMillis(subject.startLessonTime)
                    val endMillis = calendarDate.getTimeInMillis(subject.endLessonTime)
                    subject.startMillis = startMillis
                    subject.endMillis = endMillis
                }
            }
        }

        return newSchedule.originalSchedule
    }

    fun addCustomSubject(schedule: Schedule, currentWeekNumber: Int, subject: ScheduleSubject): Schedule {
        if (schedule.isNotExistSchedule()) {
            val calendar = Calendar.getInstance(Locale("ru", "BE"))
            val inputFormat = SimpleDateFormat("dd.MM.yyyy")
            schedule.startDate = inputFormat.format(calendar.time)
            calendar.add(Calendar.DATE, 7 * 4)
            schedule.endDate = inputFormat.format(calendar.time)
            fillEmptyDays(schedule, currentWeekNumber, schedule.startDate, schedule.endDate)
        }
        if (subject.startLessonTime.isNullOrEmpty() || subject.endLessonTime.isNullOrEmpty()) {
            return schedule
        }

        val calendarDate = CalendarDate(startDate = schedule.startDate)
        var daysCounter = 0

        while (!calendarDate.isEqualDate(schedule.endDate) && daysCounter != DAYS_LIMIT) {
            calendarDate.incDate(daysCounter)

            val scheduleDay = schedule.schedules.find {
                it.dateInMillis == calendarDate.getDateInMillis() &&
                        it.weekDayNumber == subject.dayNumber &&
                        subject.weekNumber?.contains(it.weekNumber) == true
            }

            if (scheduleDay != null) {
                val subjectCopy = subject.copy()
                val startMillis = calendarDate.getTimeInMillis(subject.startLessonTime)
                val endMillis = calendarDate.getTimeInMillis(subject.endLessonTime)
                subjectCopy.startMillis = startMillis
                subjectCopy.endMillis = endMillis
                subjectCopy.id = subjectCopy.hashCode()

                insertSubject(scheduleDay.schedule, arrayListOf(subjectCopy))
            }
            daysCounter++
        }

        return schedule
    }

    private fun getMillisTimeInSubjects(schedule: Schedule): Schedule {
        val newSchedule = schedule.copy()
        val calendarDate = CalendarDate(startDate = schedule.startDate)
        newSchedule.schedules.mapIndexed { index, day ->
            calendarDate.incDate(index)
            day.schedule.map { subject ->
                if (!subject.startLessonTime.isNullOrEmpty() && !subject.endLessonTime.isNullOrEmpty()) {
                    val startMillis = calendarDate.getTimeInMillis(subject.startLessonTime)
                    val endMillis = calendarDate.getTimeInMillis(subject.endLessonTime)
                    subject.startMillis = startMillis
                    subject.endMillis = endMillis
                }
            }
        }

        schedule.schedules.map { scheduleDay ->
            scheduleDay.schedule.sortBy { it.startMillis }
        }

        return newSchedule
    }

    private fun getActualSubject(schedule: Schedule): ScheduleSubject? {
        // Remove actual subject flag from all subjects
        schedule.schedules.map {
            it.schedule.map {
                it.isActual = false
            }
        }

        val dateUnixTime = Date().time
        val actualDays = schedule.schedules.filter { it.dateInMillis >= dateUnixTime - 86400000 }

        var actualSubject: ScheduleSubject? = null
        var actualSubjectIndex: Int
        for (actualDay in actualDays) {
            actualSubjectIndex = actualDay.schedule.indexOfFirst { subject ->
                subject.isIgnored != true && ((dateUnixTime < subject.startMillis) or
                        (dateUnixTime > subject.startMillis && dateUnixTime < subject.endMillis))
            }
            if (actualSubjectIndex != -1) {
                actualDay.schedule[actualSubjectIndex].isActual = true
                actualSubject = actualDay.schedule[actualSubjectIndex]
                break
            }
        }

        return actualSubject
    }

    /**
     * Function that expands schedule for all weeks, by schedule.startDate and schedule.endDate
     * @param schedule - Schedule class
     * @param currentWeekNumber - Current week number
     * @return Same Schedule class with multiplied days and subjects
     */
    private fun getMultipliedSchedule(schedule: Schedule, currentWeekNumber: Int): Schedule {
        val calendarDate = CalendarDate(startDate = schedule.startDate, currentWeekNumber)
        var daysCounter = 0
        val scheduleDays = ArrayList<ScheduleDay>()

        while (!calendarDate.isEqualDate(schedule.endDate) && daysCounter < DAYS_LIMIT) {
            calendarDate.incDate(daysCounter)
            val currentTimeInMillis = calendarDate.getDateInMillis()
            val weekNumber = calendarDate.getWeekNumber()
            val weekDayNumber = calendarDate.getWeekDayNumber()
            val weekNumberDays = schedule.schedules.filter { it.weekDayNumber == weekDayNumber }
            val weekNumberDaysCopy = weekNumberDays.map { it.copy() }
            if (weekNumberDaysCopy.isEmpty()) {
                scheduleDays.add(
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
                scheduleDays.add(
                    ScheduleDay(
                        date = calendarDate.getDateStatus(),
                        dateInMillis = calendarDate.getDateInMillis(),
                        weekDayTitle = calendarDate.getWeekDayTitle(),
                        weekDayNumber = weekDayNumber,
                        weekNumber = weekNumber,
                        schedule = subjectsCopy as ArrayList<ScheduleSubject>
                    ))
            }
            daysCounter++
        }
        schedule.schedules = scheduleDays

        mergeExamsSchedule(schedule, currentWeekNumber)

        return schedule
    }

    private fun fillEmptyDays(
        schedule: Schedule,
        currentWeekNumber: Int,
        fromDatePattern: String,
        untilDatePattern: String
    ) {
        val calendarFromDate = CalendarDate(startDate = fromDatePattern, currentWeekNumber)
        val calendarUntilDate = CalendarDate(startDate = untilDatePattern, currentWeekNumber)
        calendarUntilDate.minusDays(1)
        var daysCounter = 1

        while (calendarFromDate.getDateInMillis() < calendarUntilDate.getDateInMillis() && daysCounter < DAYS_LIMIT) {
            calendarFromDate.incDate(daysCounter)
            val weekDayNumber = calendarFromDate.getWeekDayNumber()
            val weekNumber = calendarFromDate.getWeekNumber()
            schedule.schedules.add(
                ScheduleDay(
                    date = calendarFromDate.getDateStatus(),
                    dateInMillis = calendarFromDate.getDateInMillis(),
                    weekDayTitle = calendarFromDate.getWeekDayTitle(),
                    weekDayNumber = weekDayNumber,
                    weekNumber = weekNumber,
                    schedule = ArrayList()
                ))
            daysCounter++
        }
    }

    private fun mergeExamsSchedule(schedule: Schedule, currentWeekNumber: Int) {
        if (schedule.isExamsNotExist()) return
        val calendarExamsDate = CalendarDate(startDate = schedule.startExamsDate, currentWeekNumber)

        if (!schedule.isNotExistSchedule()) {
            if (calendarExamsDate.getDateInMillis(schedule.endDate) < calendarExamsDate.getDateInMillis(schedule.startExamsDate)) {
                fillEmptyDays(schedule, currentWeekNumber, schedule.endDate, schedule.startExamsDate)
            }
        }

        for (examsDay in schedule.examsSchedule) {
            examsDay.schedule.map { filterExamSubject(it) }
            val scheduleDay = schedule.schedules.find { it.dateInMillis == examsDay.dateInMillis }
            if (scheduleDay != null) {
                insertSubject(scheduleDay.schedule, examsDay.schedule)
            } else {
                schedule.schedules.add(examsDay)
            }
        }
    }

    private fun filterExamSubject(subject: ScheduleSubject) {
        when (subject.lessonTypeAbbrev) {
            ScheduleSubject.LESSON_TYPE_LECTURE -> {
                subject.lessonTypeAbbrev = ScheduleSubject.LESSON_TYPE_CONSULTATION
            }
            ScheduleSubject.LESSON_TYPE_CONSULTATION -> {
                subject.lessonTypeAbbrev = ScheduleSubject.LESSON_TYPE_CONSULTATION
            }
            ScheduleSubject.LESSON_TYPE_PRACTISE -> {
                subject.lessonTypeAbbrev = ScheduleSubject.LESSON_TYPE_EXAM
            }
            else -> {
                subject.lessonTypeAbbrev = ScheduleSubject.LESSON_TYPE_EXAM
            }
        }
    }

    private fun insertSubject(subjectsList: ArrayList<ScheduleSubject>, examsSubjectsList: ArrayList<ScheduleSubject>) {
        for (insertSubject in examsSubjectsList) {
            val subjectIdBeforeInsert = subjectsList.indexOfFirst { it.startMillis >= insertSubject.startMillis }

            if (subjectIdBeforeInsert == -1) {
                subjectsList.add(insertSubject)
            } else {
                subjectsList.add(subjectIdBeforeInsert, insertSubject)
            }
        }
    }

//    fun getSubjectsHistoryBreakTime(scheduleDays: ArrayList<ScheduleDayUpdateHistory>): ArrayList<ScheduleDayUpdateHistory> {
//        val subjectsBreakTime = ArrayList<ScheduleDayUpdateHistory>()
//        val calendarDate = CalendarDate()
//
//        for (scheduleItem in scheduleDays) {
//            val scheduleDay = scheduleItem.copy()
//            val subjectsList = ArrayList<ScheduleSubjectHistory>()
//            for (subjectIndex in 0 until scheduleItem.scheduleActions.size) {
//                val subject = scheduleItem.scheduleActions[subjectIndex].copy()
//                if (subjectIndex != 0) {
//                    val currSubject = scheduleItem.scheduleActions[subjectIndex].scheduleSubject
//                    val prevSubject = scheduleItem.scheduleActions[subjectIndex-1].scheduleSubject
//                    subject.scheduleSubject.breakTime = calendarDate.getSubjectBreakTime(
//                        currSubject.startLessonTime,
//                        prevSubject.endLessonTime
//                    )
//                } else {
//                    subject.scheduleSubject.breakTime = SubjectBreakTime(
//                        -1,
//                        -1,
//                        false
//                    )
//                }
//                subjectsList.add(subject)
//            }
//            scheduleDay.scheduleActions = subjectsList
//            subjectsBreakTime.add(scheduleDay)
//        }
//
//        return subjectsBreakTime
//    }

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
                    var prevSubject = scheduleItem.schedule[subjectIndex - 1]
                    for (subjectIdx in subjectIndex - 1 downTo 0) {
                        if (scheduleItem.schedule[subjectIdx].isIgnored != true) {
                            prevSubject = scheduleItem.schedule[subjectIdx]
                            break
                        }
                    }
                    subject.breakTime = calendarDate.getSubjectBreakTime(
                        currSubject.startLessonTime,
                        prevSubject.endLessonTime
                    )
                } else {
                    subject.breakTime = SubjectBreakTime(
                        -1,
                        -1,
                        false
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
            day.dateInMillis = calendarDate.getDateInMillis()
            day.weekDayTitle = calendarDate.getWeekDayTitle()
            day.weekDayNumber = calendarDate.getWeekDayNumber()
            day.weekNumber = calendarDate.getWeekNumber()
            day.schedule.map { subject ->
                subject.dayNumber = calendarDate.getWeekDayNumber()
            }
        }

        return newSchedule
    }

    fun getOriginalSchedule(groupSchedule: GroupSchedule): ArrayList<ScheduleDay> {
        val originalSchedule = getNormalSchedule(groupSchedule)
        val originalWithSubjectsBreakTime = getSubjectsBreakTime(originalSchedule.schedules)

        originalSchedule.originalSchedule = originalWithSubjectsBreakTime

        return originalSchedule.originalSchedule
    }

    /** This schedule will be saved in DB */
    fun getBasicSchedule(groupSchedule: GroupSchedule, currentWeekNumber: Int): Schedule {
        val schedule = getNormalSchedule(groupSchedule)

        if (!schedule.isExamsNotExist()) {
            schedule.examsSchedule = getDaysFromSubjects(
                schedule.exams,
                schedule.startExamsDate,
                schedule.endExamsDate
            )
            schedule.examsSchedule = getSubjectsBreakTime(schedule.examsSchedule)
            schedule.examsSchedule = getScheduleBySettings(
                schedule.examsSchedule,
                false
            )
        }

        schedule.subgroups = getSubgroupsList(schedule)

        if (schedule.isNotExistSchedule()) {
            if (!schedule.isExamsNotExist()) {
                mergeExamsSchedule(schedule, currentWeekNumber)
            }
            return schedule
        }

        val scheduleMultiplied = getMultipliedSchedule(schedule, currentWeekNumber)
        setSubjectIds(schedule) // set unique id to subjects
        schedule.schedules = getSubjectsBreakTime(schedule.schedules) // set subjects break time
        val daysWithDatesSchedule = setDatesFromBeginSchedule(scheduleMultiplied, currentWeekNumber)

        setSubjectsPrediction(daysWithDatesSchedule)

        return getMillisTimeInSubjects(daysWithDatesSchedule)
    }

    /** This schedule will be shown on UI */
    fun getRegularSchedule(schedule: Schedule, ignoreSettings: Boolean): Schedule {
        val scheduleSettings = if (ignoreSettings) {
            ScheduleSettings.fullSchedule
        } else {
            schedule.settings
        }

        val regularSchedule = schedule.copy()

        if (!regularSchedule.isNotExistSchedule() || regularSchedule.schedules.isNotEmpty()) {
            if (!scheduleSettings.schedule.isShowPastDays) {
                regularSchedule.schedules = filterActualSchedule(
                    regularSchedule.schedules,
                    0,
                    schedule.startDate
                )
            } else {
                regularSchedule.schedules = filterActualSchedule(
                    regularSchedule.schedules,
                    scheduleSettings.schedule.pastDaysNumber,
                    schedule.startDate
                )
            }

            regularSchedule.schedules = setActualDateStatuses(regularSchedule.schedules, scheduleSettings.schedule.isShowPastDays)
            regularSchedule.schedules = filterBySubgroup(
                regularSchedule.schedules,
                scheduleSettings.subgroup.selectedNum
            )
            regularSchedule.schedules = getSubjectsBreakTime(regularSchedule.schedules)
            regularSchedule.subjectNow = getActualSubject(regularSchedule)
            regularSchedule.schedules = getScheduleBySettings(
                regularSchedule.schedules,
                regularSchedule.settings.schedule.isShowEmptyDays
            )
        } else {
//            regularSchedule.schedules = ArrayList()
        }

        setSubjectsPrediction(regularSchedule)

        return regularSchedule
    }

    /** FIXME subject lab for 0 subgroups bug */
    private fun setSubjectsPrediction(schedule: Schedule) {
        schedule.schedules.mapIndexed { index, day ->
            day.schedule.map { subject ->
                var i = 0
                for (dayIndex in index + 1 until schedule.schedules.size) {
                    i++
                    val scheduleDay = schedule.schedules[dayIndex]
                    val foundSubject = scheduleDay.schedule.find {
                        it.subject == subject.subject && it.lessonTypeAbbrev == subject.lessonTypeAbbrev &&
                        ((it.getEditedOrNumSubgroup()) == 0 ||
                        (it.getEditedOrNumSubgroup()) == schedule.settings.subgroup.selectedNum)
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
            val subjects = day.schedule.filter { it.getEditedOrNumSubgroup() == 0 || it.getEditedOrNumSubgroup() == subgroup }
            day.schedule = subjects as ArrayList<ScheduleSubject>
            scheduleList.add(day)
        }

        return scheduleList
    }

    // Set in ScheduleDay: "Yesterday", "Today" and "Tomorrow" values
    private fun setActualDateStatuses(schedule: ArrayList<ScheduleDay>, isShowPastDays: Boolean): ArrayList<ScheduleDay> {
        val calendarDate = CalendarDate(startDate = CalendarDate.TODAY_DATE)

        val todayStartIndex = schedule.indexOfFirst { it.dateInMillis == calendarDate.getDateInMillis() }
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
                dateInMillis = calendarDate.getDateInMillis(),
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


