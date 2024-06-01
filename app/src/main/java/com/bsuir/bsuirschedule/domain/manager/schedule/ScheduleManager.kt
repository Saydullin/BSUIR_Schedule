package com.bsuir.bsuirschedule.domain.manager.schedule

import android.provider.ContactsContract
import com.bsuir.bsuirschedule.domain.manager.schedule.builder.ScheduleDaysFromSubjects
import com.bsuir.bsuirschedule.domain.manager.schedule.builder.ScheduleExamsManager
import com.bsuir.bsuirschedule.domain.manager.schedule.builder.ScheduleFromGroupSchedule
import com.bsuir.bsuirschedule.domain.manager.schedule.builder.ScheduleMultiply
import com.bsuir.bsuirschedule.domain.manager.schedule.builder.SchedulePrediction
import com.bsuir.bsuirschedule.domain.manager.schedule.builder.ScheduleSubgroup
import com.bsuir.bsuirschedule.domain.manager.schedule.contract.ScheduleManagerContract
import com.bsuir.bsuirschedule.domain.models.Employee
import com.bsuir.bsuirschedule.domain.models.EmployeeSubject
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Holiday
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.utils.Resource

class ScheduleManager : ScheduleManagerContract {

    override fun getScheduleFromGroupSchedule(
        groupSchedule: GroupSchedule,
        weekNumber: Int
    ): Resource<Schedule> {
        val scheduleFromGroupSchedule = ScheduleFromGroupSchedule(
            groupSchedule = groupSchedule,
            currentWeekNumber = weekNumber
        )

        return scheduleFromGroupSchedule.execute()
    }

    override fun setSubgroups(scheduleDays: ArrayList<ScheduleDay>): List<Int> {
        val scheduleSubgroups = ScheduleSubgroup(
            scheduleDays = scheduleDays
        )

        return scheduleSubgroups.execute()
    }

    override fun multiplySchedule(
        scheduleDays: ArrayList<ScheduleDay>,
        holidays: ArrayList<Holiday>,
        currentWeekNumber: Int
    ): ArrayList<ScheduleDay> {
        val scheduleMultiply = ScheduleMultiply(
            scheduleDays = scheduleDays,
            holidays = holidays,
            currentWeekNumber = currentWeekNumber
        )

        return scheduleMultiply.execute()
    }

    override fun getExamsSchedule(
        examsSubjects: ArrayList<ScheduleSubject>,
        weekNumber: Int
    ): ArrayList<ScheduleDay> {
        val scheduleDaysFromSubjects = ScheduleDaysFromSubjects(
            scheduleSubjects = examsSubjects,
            currentWeekNumber = weekNumber
        )

        return scheduleDaysFromSubjects.execute()
    }

    override fun mergeSchedule(
        scheduleDays: ArrayList<ScheduleDay>,
        examsDays: ArrayList<ScheduleDay>,
        weekNumber: Int
    ): ArrayList<ScheduleDay> {
        val examsScheduleManager = ScheduleExamsManager()
        val mergedDays = examsScheduleManager.mergeScheduleAndExams(
            scheduleDays = scheduleDays,
            examsDays = examsDays,
            weekNumber = weekNumber
        )

        return mergedDays
    }

    override fun setSubjectsUniqueId(scheduleDays: ArrayList<ScheduleDay>): ArrayList<ScheduleDay> {
        var idCounter = 0

        return scheduleDays.map { day ->
            day.copy(schedule = day.schedule.map { subject ->
                subject.copy(id = idCounter++)
            } as ArrayList<ScheduleSubject>)
        } as ArrayList<ScheduleDay>
    }

    override fun setPrediction(scheduleDays: ArrayList<ScheduleDay>): ArrayList<ScheduleDay> {
        val schedulePrediction = SchedulePrediction(
            scheduleDays = scheduleDays
        )

        return schedulePrediction.execute()
    }

    override fun injectGroupsInSubjects(
        scheduleDays: ArrayList<ScheduleDay>,
        groups: ArrayList<Group>,
    ): ArrayList<ScheduleDay> {
        return scheduleDays.map { day ->
            day.copy(
                schedule = day.schedule.map { subject ->
                    val subjectGroups = (subject.subjectGroups?.mapNotNull { subjectGroupName ->
                        groups.find { it.name == subjectGroupName.name }
                    } ?: emptyList()) as ArrayList<Group>
                    subject.copy(groups = subjectGroups)
                } as ArrayList<ScheduleSubject>
            )
        } as ArrayList<ScheduleDay>
    }

    override fun injectEmployeesInSubjects(
        scheduleDays: ArrayList<ScheduleDay>,
        employees: ArrayList<Employee>,
    ): ArrayList<ScheduleDay> {
        return scheduleDays.map { day ->
            day.copy(
                schedule = day.schedule.map { subject ->
                    subject.copy(
                        employees = (subject.employees?.mapNotNull { employeeItem ->
                            employees.find { it.id == employeeItem.id }
                                ?.toEmployeeSubject()
                                ?.copy(email = employeeItem.email)
                        } ?: emptyList()) as ArrayList<EmployeeSubject>
                    )
                } as ArrayList<ScheduleSubject>
            )
        } as ArrayList<ScheduleDay>
    }

}


