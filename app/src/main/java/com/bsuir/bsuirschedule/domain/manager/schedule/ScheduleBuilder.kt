package com.bsuir.bsuirschedule.domain.manager.schedule

import android.util.Log
import com.bsuir.bsuirschedule.domain.manager.schedule.component.ScheduleEmptyDays
import com.bsuir.bsuirschedule.domain.manager.schedule.component.SubjectHoursCounter
import com.bsuir.bsuirschedule.domain.models.Employee
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Holiday
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleTerm
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.domain.utils.CalendarInstance
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.StatusCode
import java.text.SimpleDateFormat
import java.util.Calendar

class ScheduleBuilder {

    private var mGroupSchedule: GroupSchedule? = null
    private var mScheduleSettings: ScheduleSettings? = null
    private var mEmployees: ArrayList<Employee> = arrayListOf()
    private var mGroups: ArrayList<Group> = arrayListOf()
    private var mHolidays: ArrayList<Holiday> = arrayListOf()
    private var mWeekNumber: Int = 0
    private var mSetPreviousSchedule: Boolean = false

    fun setGroupSchedule(groupSchedule: GroupSchedule): ScheduleBuilder {
        mGroupSchedule = groupSchedule
        return this
    }

    fun setHolidays(holidays: ArrayList<Holiday>): ScheduleBuilder {
        mHolidays = holidays
        return this
    }

    fun setCurrentWeekNumber(weekNumber: Int): ScheduleBuilder {
        mWeekNumber = weekNumber
        return this
    }

    fun injectEmployees(employees: ArrayList<Employee>): ScheduleBuilder {
        mEmployees = employees
        return this
    }

    fun injectGroups(groups: ArrayList<Group>): ScheduleBuilder {
        mGroups = groups
        return this
    }

    fun setSettings(scheduleSettings: ScheduleSettings?): ScheduleBuilder {
        mScheduleSettings = scheduleSettings
        return this
    }

    fun setPreviousSchedule(showPreviousSchedule: Boolean = true): ScheduleBuilder {
        mSetPreviousSchedule = showPreviousSchedule
        return this
    }

    private fun injectDTOInSubjects(
        scheduleManager: ScheduleManager,
        scheduleDays: ArrayList<ScheduleDay>,
    ): ArrayList<ScheduleDay> {
        val employeesInjected = if (mEmployees.isNotEmpty()) {
            scheduleManager.injectEmployeesInSubjects(
                scheduleDays = scheduleDays,
                employees = mEmployees,
            )
        } else {
            scheduleDays
        }

        val groupsInjected = if (mGroups.isNotEmpty()) {
            scheduleManager.injectGroupsInSubjects(
                scheduleDays = scheduleDays,
                groups = mGroups,
            )
        } else {
            employeesInjected
        }

        return groupsInjected
    }

    private fun scheduleFilterBySettings(
        scheduleSettings: ScheduleSettings,
        scheduleManager: ScheduleManager,
        scheduleDays: ArrayList<ScheduleDay>,
        startDate: String,
    ): ArrayList<ScheduleDay> {

        val filteredActualDays = scheduleManager.filterSchedulePastDaysBySettings(
            scheduleSettings = scheduleSettings.schedule,
            scheduleDays = scheduleDays,
            startDate = startDate
        )

        val filteredActualDates = scheduleManager.filterScheduleDatesBySettings(
            scheduleSettings = scheduleSettings.schedule,
            scheduleDays = filteredActualDays,
        )

        val filteredSubgroups = scheduleManager.filterScheduleSubgroupBySettings(
            scheduleSettings = scheduleSettings,
            scheduleDays = filteredActualDates
        )

        return filteredSubgroups
    }

    private fun initSchedule(
        scheduleManager: ScheduleManager,
        scheduleDays: ArrayList<ScheduleDay>,
        weekNumber: Int,
        startDate: String,
        endDate: String,
        scheduleSettings: ScheduleSettings? = null,
        holidays: ArrayList<Holiday> = arrayListOf(),
    ): ArrayList<ScheduleDay> {
        val examsEmployeeAndGroupsInjected = injectDTOInSubjects(
            scheduleManager = scheduleManager,
            scheduleDays = scheduleDays,
        )

        val calendar = CalendarInstance.get()
        calendar.add(Calendar.DATE, 21)
        val output = SimpleDateFormat("dd.MM.yyyy")
        val todayEndDate = output.format(calendar.time)

        val multipliedDays = scheduleManager.multiplySchedule(
            scheduleDays = examsEmployeeAndGroupsInjected,
            holidays = holidays,
            currentWeekNumber = weekNumber,
            startDate = startDate,
            endDate = todayEndDate,
        )

        val scheduleDayWithUniqueSubjectId = scheduleManager.setSubjectsUniqueId(
            scheduleDays = multipliedDays
        )

        val scheduleWithSubjectBreakTime = scheduleManager.setSubjectsBreakTime(
            scheduleDays = scheduleDayWithUniqueSubjectId
        )

        val removeEmptyDays = if (scheduleSettings?.schedule?.isShowEmptyDays == false) {
                val removeEmptyDays = ScheduleEmptyDays(
                    scheduleDays = scheduleWithSubjectBreakTime
                )
                removeEmptyDays.execute()
            } else {
                scheduleWithSubjectBreakTime
            }

        val filteredScheduleBySettings = if (scheduleSettings != null) {
            scheduleFilterBySettings(
                scheduleManager = scheduleManager,
                scheduleSettings = scheduleSettings,
                scheduleDays = removeEmptyDays,
                startDate = startDate
            )
        } else {
            removeEmptyDays
        }

        val subjectHoursCounter = SubjectHoursCounter()

        return subjectHoursCounter.execute(filteredScheduleBySettings)
    }

    private fun initExamsSchedule(
        scheduleManager: ScheduleManager,
        scheduleDays: ArrayList<ScheduleDay>,
    ): ArrayList<ScheduleDay> {
        val examsEmployeeAndGroupsInjected = injectDTOInSubjects(
            scheduleManager = scheduleManager,
            scheduleDays = scheduleDays,
        )

        val scheduleDayWithUniqueSubjectId = scheduleManager.setSubjectsUniqueId(
            scheduleDays = examsEmployeeAndGroupsInjected
        )

        val scheduleWithSubjectBreakTime = scheduleManager.setSubjectsBreakTime(
            scheduleDays = scheduleDayWithUniqueSubjectId
        )

        val subjectHoursCounter = SubjectHoursCounter()

        return subjectHoursCounter.execute(scheduleWithSubjectBreakTime)
    }

    private fun checkIsEmptyAllDays(scheduleDays: ArrayList<ScheduleDay>): Boolean {
        return scheduleDays.all { it.schedule.isEmpty() }
    }

    fun build(): Resource<Schedule> {
        val groupSchedule = mGroupSchedule
            ?: throw IllegalStateException("GroupSchedule must be non-null")

        val scheduleManager = ScheduleManager()

        val scheduleRes = scheduleManager.getScheduleFromGroupSchedule(
            groupSchedule = groupSchedule,
            weekNumber = mWeekNumber
        )

        if (scheduleRes is Resource.Error) {
            return Resource.Error(
                statusCode = scheduleRes.statusCode
            )
        }

        if (scheduleRes.data == null) {
            return Resource.Error(
                statusCode = StatusCode.DATA_ERROR,
            )
        }

        // exams

        val schedule = scheduleRes.data
        val scheduleDays = schedule.schedules
        val previousScheduleDays = schedule.previousSchedules

        Log.e("sady", "BUILDING: ${scheduleRes.data.previousSchedules}")

        val examsDays = scheduleManager.getExamsSchedule(
            examsSubjects = schedule.exams,
            weekNumber = mWeekNumber,
        )

        val examsScheduleDays = injectDTOInSubjects(
            scheduleManager = scheduleManager,
            scheduleDays = examsDays,
        )

        val startScheduleDate = scheduleManager.getStartDate(
            scheduleDays = scheduleDays
        )
        val endScheduleDate = scheduleManager.getEndDate(
            scheduleDays = scheduleDays
        )
        val startPreviousScheduleDate = scheduleManager.getStartDate(
            scheduleDays = previousScheduleDays
        )
        val endPreviousScheduleDate = scheduleManager.getEndDate(
            scheduleDays = previousScheduleDays
        )
        val startExamsDate = scheduleManager.getExamsStartDate(
            examsDays = examsScheduleDays
        )
        val endExamsDate = scheduleManager.getExamsEndDate(
            examsDays = examsScheduleDays
        )

        val scheduleSubgroups = scheduleManager.setSubgroups(scheduleDays)
        val previousScheduleSubgroups = scheduleManager.setSubgroups(previousScheduleDays)
        val subgroups = if (scheduleSubgroups.size > previousScheduleSubgroups.size) {
            scheduleSubgroups
        } else {
            previousScheduleSubgroups
        }

        val initSchedule = if (!checkIsEmptyAllDays(scheduleDays) &&
            !startScheduleDate.isNullOrEmpty() && !endScheduleDate.isNullOrEmpty()) {
            initSchedule(
                scheduleManager = scheduleManager,
                scheduleDays = scheduleDays,
                weekNumber = mWeekNumber,
                startDate = startScheduleDate,
                endDate = endScheduleDate,
                scheduleSettings = mScheduleSettings,
                holidays = mHolidays,
            )
        } else {
            arrayListOf()
        }
        val initPreviousSchedule = if (!checkIsEmptyAllDays(previousScheduleDays)
            && !startPreviousScheduleDate.isNullOrEmpty() && !endPreviousScheduleDate.isNullOrEmpty()) {
            initSchedule(
                scheduleManager = scheduleManager,
                scheduleDays = previousScheduleDays,
                weekNumber = mWeekNumber,
                startDate = startPreviousScheduleDate,
                endDate = endPreviousScheduleDate,
                scheduleSettings = null,
                holidays = mHolidays,
            )
        } else {
            arrayListOf()
        }
        val initExamsSchedule = if (!checkIsEmptyAllDays(examsScheduleDays)
            && !startExamsDate.isNullOrEmpty()) {
            initExamsSchedule(
                scheduleManager = scheduleManager,
                scheduleDays = examsScheduleDays,
            )
        } else {
            arrayListOf()
        }

        schedule.settings = mScheduleSettings ?: ScheduleSettings.empty

        Log.e("sady", "ScheduleTerm before ${schedule.settings.term}")
        if (schedule.settings.term.selectedTerm == ScheduleTerm.NOTHING) {
            if (startScheduleDate.isNullOrEmpty()) {
                if (!schedule.isExamsNotExist()) {
                    schedule.settings.term.selectedTerm = ScheduleTerm.SESSION
                }
            } else {
                if (!checkIsEmptyAllDays(schedule.schedules)) {
                    schedule.settings.term.selectedTerm = ScheduleTerm.CURRENT_SCHEDULE
                } else if (!checkIsEmptyAllDays(schedule.previousSchedules)) {
                    schedule.settings.term.selectedTerm = ScheduleTerm.PREVIOUS_SCHEDULE
                }
            }
        }
        Log.e("sady", "startScheduleDate $startScheduleDate")
        Log.e("sady", "endScheduleDate $endScheduleDate")
        Log.e("sady", "schedules $initSchedule")

        schedule.subgroups = subgroups
        schedule.schedules = initSchedule
        schedule.previousSchedules = initPreviousSchedule
        schedule.examsSchedule = initExamsSchedule
        schedule.startDate = startScheduleDate ?: startPreviousScheduleDate ?: ""
        schedule.endDate = endScheduleDate ?: endPreviousScheduleDate ?: ""
        schedule.startExamsDate = startExamsDate ?: ""
        schedule.endExamsDate = endExamsDate ?: ""

        return Resource.Success(schedule)
    }

}


