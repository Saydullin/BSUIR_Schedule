package com.bsuir.bsuirschedule.domain.manager.schedule

import com.bsuir.bsuirschedule.domain.models.Employee
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Holiday
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.StatusCode

class ScheduleBuilder {

    private var mGroupSchedule: GroupSchedule? = null
    private var mEmployees: ArrayList<Employee> = arrayListOf()
    private var mGroups: ArrayList<Group> = arrayListOf()
    private var mHolidays: ArrayList<Holiday> = arrayListOf()
    private var mWeekNumber: Int = 0

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

    fun injectDTOInSubjects(
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

        val schedule = scheduleRes.data
        val scheduleDays = schedule.schedules
        val subgroups = scheduleManager.setSubgroups(scheduleDays)

        val employeeAndGroupsInjected = injectDTOInSubjects(
            scheduleManager = scheduleManager,
            scheduleDays = scheduleDays,
        )
        val multipliedDays = scheduleManager.multiplySchedule(
            scheduleDays = employeeAndGroupsInjected,
            holidays = mHolidays,
            currentWeekNumber = mWeekNumber,
        )
        val examsDays = scheduleManager.getExamsSchedule(
            examsSubjects = groupSchedule.exams ?: arrayListOf(),
            weekNumber = mWeekNumber,
        )
        val examsEmployeeAndGroupsInjected = injectDTOInSubjects(
            scheduleManager = scheduleManager,
            scheduleDays = examsDays,
        )
        val mergedDays = scheduleManager.mergeSchedule(
            scheduleDays = multipliedDays,
            examsDays = examsEmployeeAndGroupsInjected,
            weekNumber = mWeekNumber
        )
        val scheduleDayWithUniqueSubjectId = scheduleManager.setSubjectsUniqueId(
            scheduleDays = mergedDays
        )
        val predictionSchedule = scheduleManager.setPrediction(
            scheduleDays = scheduleDayWithUniqueSubjectId
        )

        schedule.subgroups = subgroups
        schedule.schedules = predictionSchedule

        return Resource.Success(schedule)
    }

}


