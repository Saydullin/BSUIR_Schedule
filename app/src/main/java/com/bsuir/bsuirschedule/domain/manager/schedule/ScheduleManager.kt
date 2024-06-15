package com.bsuir.bsuirschedule.domain.manager.schedule

import com.bsuir.bsuirschedule.domain.manager.schedule.component.ExamsScheduleEndDateFromSubjects
import com.bsuir.bsuirschedule.domain.manager.schedule.component.ExamsScheduleStartDateFromSubjects
import com.bsuir.bsuirschedule.domain.manager.schedule.component.ScheduleBreakTime
import com.bsuir.bsuirschedule.domain.manager.schedule.component.ScheduleDaysFromSubjects
import com.bsuir.bsuirschedule.domain.manager.schedule.component.ScheduleEmptyDays
import com.bsuir.bsuirschedule.domain.manager.schedule.component.ScheduleExamsManager
import com.bsuir.bsuirschedule.domain.manager.schedule.component.ScheduleSettingsPastDays
import com.bsuir.bsuirschedule.domain.manager.schedule.component.ScheduleFromGroupSchedule
import com.bsuir.bsuirschedule.domain.manager.schedule.component.ScheduleMultiply
import com.bsuir.bsuirschedule.domain.manager.schedule.component.SchedulePrediction
import com.bsuir.bsuirschedule.domain.manager.schedule.component.ScheduleSettingsActualDates
import com.bsuir.bsuirschedule.domain.manager.schedule.component.ScheduleSettingsSubgroup
import com.bsuir.bsuirschedule.domain.manager.schedule.component.ScheduleSubgroup
import com.bsuir.bsuirschedule.domain.manager.schedule.contract.ScheduleManagerContract
import com.bsuir.bsuirschedule.domain.models.Employee
import com.bsuir.bsuirschedule.domain.models.EmployeeSubject
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Holiday
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettingsSchedule
import com.bsuir.bsuirschedule.domain.utils.Resource
import java.text.SimpleDateFormat

class ScheduleManager : ScheduleManagerContract {

    override fun getScheduleFromGroupSchedule(
        groupSchedule: GroupSchedule,
        weekNumber: Int
    ): Resource<Schedule> {
        val scheduleFromGroupSchedule = ScheduleFromGroupSchedule(
            groupSchedule = groupSchedule
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
        currentWeekNumber: Int,
        startDate: String,
        endDate: String,
    ): ArrayList<ScheduleDay> {
        val scheduleMultiply = ScheduleMultiply(
            scheduleDays = scheduleDays,
            holidays = holidays,
            currentWeekNumber = currentWeekNumber,
            startDate = startDate,
            endDate = endDate
        )

        return scheduleMultiply.execute()
    }

    override fun getExamsSchedule(
        examsSubjects: ArrayList<ScheduleSubject>,
        weekNumber: Int,
    ): ArrayList<ScheduleDay> {
        val startDate = ExamsScheduleStartDateFromSubjects(
            scheduleSubjects = examsSubjects
        ).execute()
        val endDate = ExamsScheduleEndDateFromSubjects(
            scheduleSubjects = examsSubjects
        ).execute()
        val scheduleDaysFromSubjects = ScheduleDaysFromSubjects(
            scheduleSubjects = examsSubjects,
            currentWeekNumber = weekNumber,
            startDate = startDate,
            endDate = endDate
        )
        val scheduleDays = scheduleDaysFromSubjects.execute()
        val removeEmptyDays = ScheduleEmptyDays(
            scheduleDays = scheduleDays
        )
        val emptyDays = removeEmptyDays.execute()
        emptyDays.map { day ->
            day.schedule.sortBy { it.startLessonTime }
        }
        val breakTimeSchedule = setSubjectsBreakTime(
            scheduleDays = emptyDays
        )

        return breakTimeSchedule
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

    override fun setSubjectsBreakTime(scheduleDays: ArrayList<ScheduleDay>): ArrayList<ScheduleDay> {
        val scheduleBreakTime = ScheduleBreakTime(
            scheduleDays = scheduleDays
        )

        return scheduleBreakTime.execute()
    }

    override fun filterActualScheduleBySettings(
        scheduleSettings: ScheduleSettingsSchedule,
        scheduleDays: ArrayList<ScheduleDay>,
        startDate: String,
    ): ArrayList<ScheduleDay> {
        val scheduleSettingsPastDays = ScheduleSettingsPastDays(
            scheduleSettings = scheduleSettings,
            scheduleDays = scheduleDays,
            startDate = startDate,
        )

        return scheduleSettingsPastDays.execute()
    }

    override fun filterScheduleDatesBySettings(
        scheduleSettings: ScheduleSettingsSchedule,
        scheduleDays: ArrayList<ScheduleDay>,
    ): ArrayList<ScheduleDay> {
        val scheduleSettingsActualDates = ScheduleSettingsActualDates(
            scheduleSettings = scheduleSettings,
            scheduleDays = scheduleDays,
        )

        return scheduleSettingsActualDates.execute()
    }

    override fun filterScheduleSubgroupBySettings(
        scheduleSettings: ScheduleSettings,
        scheduleDays: ArrayList<ScheduleDay>
    ): ArrayList<ScheduleDay> {
        val scheduleSettingsSubgroup = ScheduleSettingsSubgroup(
            scheduleSettings = scheduleSettings,
            scheduleDays = scheduleDays,
        )

        return scheduleSettingsSubgroup.execute()
    }

    override fun getStartDate(scheduleDays: ArrayList<ScheduleDay>): String? {
        val subjectsList = scheduleDays.flatMap { it.schedule }
        val firstSubject = subjectsList.maxByOrNull { subject ->
            val inputFormat = SimpleDateFormat("dd.MM.yyyy")
            val subjectStartLessonDate = subject.startLessonDate
            if (subjectStartLessonDate.isNullOrEmpty()) {
                if (subject.dateLesson.isNullOrEmpty()) {
                    0
                } else {
                    (inputFormat.parse(subject.dateLesson)?.time ?: 0) * -1
                }
            } else {
                (inputFormat.parse(subjectStartLessonDate)?.time ?: 0) * -1
            }
        }

        return firstSubject?.startLessonDate
    }

    override fun getEndDate(scheduleDays: ArrayList<ScheduleDay>): String? {
        val subjectsList = scheduleDays.flatMap { it.schedule }
        val lastSubject = subjectsList.maxByOrNull { subject ->
            val inputFormat = SimpleDateFormat("dd.MM.yyyy")
            val subjectStartLessonDate = subject.endLessonDate
            if (subjectStartLessonDate.isNullOrEmpty()) {
                if (subject.dateLesson.isNullOrEmpty()) {
                    0
                } else {
                    inputFormat.parse(subject.dateLesson)?.time ?: 0
                }
            } else {
                inputFormat.parse(subjectStartLessonDate)?.time ?: 0
            }
        }

        return lastSubject?.endLessonDate
    }

    override fun getExamsStartDate(examsDays: ArrayList<ScheduleDay>): String? {
        val examsStartDate = ExamsScheduleStartDateFromSubjects(
            scheduleSubjects = examsDays.flatMap { it.schedule } as ArrayList<ScheduleSubject>
        )

        return examsStartDate.execute()
    }

    override fun getExamsEndDate(examsDays: ArrayList<ScheduleDay>): String? {
        val examsEndDate = ExamsScheduleEndDateFromSubjects(
            scheduleSubjects = examsDays.flatMap { it.schedule } as ArrayList<ScheduleSubject>
        )

        return examsEndDate.execute()
    }

}


