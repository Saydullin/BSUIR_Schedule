package com.bsuir.bsuirschedule.domain.usecase.schedule

import android.util.Log
import com.bsuir.bsuirschedule.domain.manager.schedule.component.ScheduleBreakTime
import com.bsuir.bsuirschedule.domain.models.EmployeeSubject
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.GroupSubject
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleTerm
import com.bsuir.bsuirschedule.domain.usecase.GetCurrentWeekUseCase
import com.bsuir.bsuirschedule.domain.usecase.GetEmployeeItemsUseCase
import com.bsuir.bsuirschedule.domain.usecase.GetGroupItemsUseCase
import com.bsuir.bsuirschedule.domain.utils.CalendarDate
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.ScheduleController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddSubjectUseCase(
    private val getScheduleUseCase: GetScheduleUseCase,
    private val saveScheduleUseCase: SaveScheduleUseCase,
    private val getCurrentWeekUseCase: GetCurrentWeekUseCase,
    private val getGroupItemsUseCase: GetGroupItemsUseCase,
    private val getEmployeeItemsUseCase: GetEmployeeItemsUseCase,
) {

    suspend fun execute(
        scheduleId: Int,
        subject: ScheduleSubject,
        sourceItemsText: String,
        scheduleTerm: ScheduleTerm,
    ): Resource<Schedule> {
        Log.e("sady", "begin $scheduleTerm")
        Log.e("sady", "getScheduleUseCase executing")
        val scheduleResource = getScheduleUseCase.getById(scheduleId)
        Log.e("sady", "getCurrentWeekUseCase executing")
        val currentWeekResource = getCurrentWeekUseCase.getCurrentWeek()

        Log.e("sady", "currentWeekResource executing")
        val currentWeekNumber = currentWeekResource.data ?: return Resource.Error(
            statusCode = currentWeekResource.statusCode,
            message = currentWeekResource.message
        )

        Log.e("sady", "scheduleResource executing")
        var schedule = scheduleResource.data ?: return Resource.Error(
            statusCode = scheduleResource.statusCode,
            message = scheduleResource.message
        )

        Log.e("sady", "setSubjectSourceItems executing: $sourceItemsText")
        setSubjectSourceItems(schedule.isGroup(), sourceItemsText, subject)

        when(scheduleTerm) {
            ScheduleTerm.CURRENT_SCHEDULE -> {
                schedule = insertSubjectInSchedule(
                    schedule = scheduleResource.data,
                    currentWeekNumber = currentWeekNumber,
                    subject = subject,
                )
            }
            ScheduleTerm.PREVIOUS_SCHEDULE -> {
                schedule = insertSubjectInPreviousSchedule(
                    schedule = scheduleResource.data,
                    currentWeekNumber = currentWeekNumber,
                    subject = subject,
                )
            }
            ScheduleTerm.SESSION -> {
                schedule = insertSubjectInExamsSchedule(
                    schedule = scheduleResource.data,
                    subject = subject,
                )
            }
            else -> {}
        }

        Log.e("sady", "savedScheduleResource executing ${schedule.previousSchedules}")
        val savedScheduleResource = saveScheduleUseCase.execute(schedule)

        if (savedScheduleResource is Resource.Error) {
            Log.e("sady", "saving schedule issues, ${savedScheduleResource.message}")
            return Resource.Error(
                statusCode = savedScheduleResource.statusCode,
                message = savedScheduleResource.message
            )
        }

        Log.e("sady", "finished")
        return Resource.Success(schedule)
    }

    private suspend fun setSubjectSourceItems(isGroup: Boolean, sourceItemsText: String, subject: ScheduleSubject) {
        val sourceItems = sourceItemsText
            .replace(" ", "")
            .replace(",", " ")
            .split(" ")

        if (sourceItems.isEmpty()) return
        if (isGroup) {
            val employeeItems = ArrayList<EmployeeSubject>()
            sourceItems.map { item ->
                val employeeItemResult = getEmployeeItemsUseCase.getEmployeeItems()
                if (employeeItemResult is Resource.Success && !employeeItemResult.data.isNullOrEmpty()) {
                    val employeeItem = employeeItemResult.data.firstOrNull {
                        if (it.getName().isNotBlank()) {
                            it.getName().replace(" ", "") == item
                        } else {
                            false
                        }
                    }
                    if (employeeItem != null) {
                        employeeItems.add(employeeItem.toEmployeeSubject())
                    }
                }
            }
            subject.employees = employeeItems
        } else {
            val groupItems = ArrayList<Group>()
            val subjectGroupsItems = ArrayList<GroupSubject>()
            sourceItems.map { item ->
                val groupItemResult = getGroupItemsUseCase.getAllGroupItems()
                if (groupItemResult is Resource.Success && !groupItemResult.data.isNullOrEmpty()) {
                    val groupItem = groupItemResult.data.firstOrNull {
                        it.name.replace(" ", "") == item
                    }
                    if (groupItem != null) {
                        groupItems.add(groupItem)
                        subjectGroupsItems.add(
                            GroupSubject(
                            specialityName = groupItem.speciality?.name ?: "",
                            specialityCode = groupItem.speciality?.code ?: "",
                            numberOfStudents = 0,
                            name = groupItem.name,
                            educationDegree = 0,
                        )
                        )
                    }
                }
            }
            subject.groups = groupItems
            subject.subjectGroups = subjectGroupsItems
        }
    }

    private fun insertSubjectInPreviousSchedule(
        schedule: Schedule,
        currentWeekNumber: Int,
        subject: ScheduleSubject,
    ): Schedule {
        if (schedule.previousSchedules.isEmpty()) {
            val calendar = Calendar.getInstance(Locale("ru", "BE"))
            val inputFormat = SimpleDateFormat("dd.MM.yyyy")
            schedule.startDate = inputFormat.format(calendar.time)
            calendar.add(Calendar.DATE, 7 * 4)
            schedule.endDate = inputFormat.format(calendar.time)
            fillEmptyDays(
                schedule,
                currentWeekNumber,
                schedule.startDate,
                schedule.endDate,
                scheduleTerm = ScheduleTerm.PREVIOUS_SCHEDULE
            )
        }
        if (subject.startLessonTime.isNullOrEmpty() || subject.endLessonTime.isNullOrEmpty()) {
            return schedule
        }

        val calendarDate = CalendarDate(startDate = schedule.startDate)
        var daysCounter = 0

        while (!calendarDate.isEqualDate(schedule.endDate) && daysCounter != ScheduleController.DAYS_LIMIT) {
            calendarDate.incDate(daysCounter)

            val scheduleDay = schedule.previousSchedules.find {
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

        schedule.previousSchedules.map { day ->
            day.schedule.sortBy { it.startLessonTime }
        }

        val scheduleBreakTime = ScheduleBreakTime(
            scheduleDays = schedule.previousSchedules
        )
        val scheduleDaysWithBreakTime = scheduleBreakTime.execute()

        schedule.previousSchedules = scheduleDaysWithBreakTime

        return schedule
    }

    private fun insertSubjectInSchedule(
        schedule: Schedule,
        currentWeekNumber: Int,
        subject: ScheduleSubject,
    ): Schedule {
        if (schedule.isNotExistSchedule()) {
            val calendar = Calendar.getInstance(Locale("ru", "BE"))
            val inputFormat = SimpleDateFormat("dd.MM.yyyy")
            schedule.startDate = inputFormat.format(calendar.time)
            calendar.add(Calendar.DATE, 7 * 4)
            schedule.endDate = inputFormat.format(calendar.time)
            fillEmptyDays(
                schedule,
                currentWeekNumber,
                schedule.startDate,
                schedule.endDate,
                scheduleTerm = ScheduleTerm.CURRENT_SCHEDULE
            )
        }
        if (subject.startLessonTime.isNullOrEmpty() || subject.endLessonTime.isNullOrEmpty()) {
            return schedule
        }

        val calendarDate = CalendarDate(startDate = schedule.startDate)
        var daysCounter = 0

        while (!calendarDate.isEqualDate(schedule.endDate) && daysCounter != ScheduleController.DAYS_LIMIT) {
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

        schedule.schedules.map { day ->
            day.schedule.sortBy { it.startLessonTime }
        }

        val scheduleBreakTime = ScheduleBreakTime(
            scheduleDays = schedule.schedules
        )
        val scheduleDaysWithBreakTime = scheduleBreakTime.execute()

        schedule.schedules = scheduleDaysWithBreakTime

        return schedule
    }

    private fun insertSubjectInExamsSchedule(
        schedule: Schedule,
        subject: ScheduleSubject,
    ): Schedule {
        if (schedule.isExamsNotExist()) {
            val calendar = Calendar.getInstance(Locale("ru", "BE"))
            val inputFormat = SimpleDateFormat("dd.MM.yyyy")
            schedule.startDate = inputFormat.format(calendar.time)
            calendar.add(Calendar.DATE, 7 * 4)
            schedule.endDate = inputFormat.format(calendar.time)
        }
        if (subject.startLessonTime.isNullOrEmpty() || subject.endLessonTime.isNullOrEmpty()) {
            return schedule
        }

        val calendarDate = CalendarDate(startDate = schedule.startDate)
        var daysCounter = 0

        while (!calendarDate.isEqualDate(schedule.endDate) && daysCounter != ScheduleController.DAYS_LIMIT) {
            calendarDate.incDate(daysCounter)

            val scheduleDay = schedule.examsSchedule.find {
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

        schedule.examsSchedule.map { day ->
            day.schedule.sortBy { it.startLessonTime }
        }

        val scheduleBreakTime = ScheduleBreakTime(
            scheduleDays = schedule.examsSchedule
        )
        val scheduleDaysWithBreakTime = scheduleBreakTime.execute()

        schedule.examsSchedule = scheduleDaysWithBreakTime

        return schedule
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

    private fun fillEmptyDays(
        schedule: Schedule,
        currentWeekNumber: Int,
        fromDatePattern: String,
        untilDatePattern: String,
        scheduleTerm: ScheduleTerm = ScheduleTerm.CURRENT_SCHEDULE,
    ) {
        val calendarFromDate = CalendarDate(startDate = fromDatePattern, currentWeekNumber)
        val calendarUntilDate = CalendarDate(startDate = untilDatePattern, currentWeekNumber)
        calendarUntilDate.minusDays(1)
        var daysCounter = 1

        while (calendarFromDate.getDateInMillis() < calendarUntilDate.getDateInMillis() && daysCounter < ScheduleController.DAYS_LIMIT) {
            calendarFromDate.incDate(daysCounter)
            val weekDayNumber = calendarFromDate.getWeekDayNumber()
            val weekNumber = calendarFromDate.getWeekNum()
            when(scheduleTerm) {
                ScheduleTerm.PREVIOUS_SCHEDULE -> {
                    schedule.previousSchedules.add(
                        ScheduleDay(
                            date = calendarFromDate.getDateStatus(),
                            dateInMillis = calendarFromDate.getDateInMillis(),
                            weekDayTitle = calendarFromDate.getWeekDayTitle(),
                            weekDayNumber = weekDayNumber,
                            weekNumber = weekNumber,
                            schedule = ArrayList()
                        ))
                }
                ScheduleTerm.CURRENT_SCHEDULE -> {
                    schedule.schedules.add(
                        ScheduleDay(
                            date = calendarFromDate.getDateStatus(),
                            dateInMillis = calendarFromDate.getDateInMillis(),
                            weekDayTitle = calendarFromDate.getWeekDayTitle(),
                            weekDayNumber = weekDayNumber,
                            weekNumber = weekNumber,
                            schedule = ArrayList()
                        ))
                }
                else -> {}
            }
            daysCounter++
        }
    }

}


