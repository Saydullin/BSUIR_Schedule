package com.bsuir.bsuirschedule.domain.manager.schedule.contract

import com.bsuir.bsuirschedule.domain.models.Employee
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Holiday
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettingsSchedule
import com.bsuir.bsuirschedule.domain.utils.Resource

interface ScheduleManagerContract {

    /**
     * Getting data in more comfortable model:
     * GroupSchedule(API) -> Schedule(Model).
     *
     * This function must called first of all, unlike other functions in schedule build hierarchy
     *
     * @param groupSchedule initial model from server
     * @param weekNumber current week number
     *
     * @return Schedule model (wrapped in Resource class in case error while calculating)
     */
    fun getScheduleFromGroupSchedule(
        groupSchedule: GroupSchedule,
        weekNumber: Int
    ): Resource<Schedule>

    fun getExamsSchedule(
        examsSubjects: ArrayList<ScheduleSubject>,
        weekNumber: Int,
    ): ArrayList<ScheduleDay>

    /**
     * Merges schedule with exams
     * @param scheduleDays schedule days
     * @param examsDays exams days
     * @param weekNumber current week number
     *
     * @return Merged arrayList of ScheduleDay
     */
    fun mergeSchedule(
        scheduleDays: ArrayList<ScheduleDay>,
        examsDays: ArrayList<ScheduleDay>,
        weekNumber: Int
    ): ArrayList<ScheduleDay>

    fun setSubgroups(
        scheduleDays: ArrayList<ScheduleDay>
    ): List<Int>

    fun multiplySchedule(
        scheduleDays: ArrayList<ScheduleDay>,
        holidays: ArrayList<Holiday>,
        currentWeekNumber: Int,
        startDate: String,
        endDate: String,
    ): ArrayList<ScheduleDay>

    fun setSubjectsUniqueId(
        scheduleDays: ArrayList<ScheduleDay>
    ): ArrayList<ScheduleDay>

    fun setPrediction(
        scheduleDays: ArrayList<ScheduleDay>
    ): ArrayList<ScheduleDay>

    fun injectGroupsInSubjects(
        scheduleDays: ArrayList<ScheduleDay>,
        groups: ArrayList<Group>
    ): ArrayList<ScheduleDay>

    fun injectEmployeesInSubjects(
        scheduleDays: ArrayList<ScheduleDay>,
        employees: ArrayList<Employee>
    ): ArrayList<ScheduleDay>

    fun setSubjectsBreakTime(
        scheduleDays: ArrayList<ScheduleDay>
    ): ArrayList<ScheduleDay>

    /**
     * Get actual schedule.
     *
     * Filters how many past days must be shown
     *
     * @param scheduleSettings schedule settings where specified past days amount
     * @param scheduleDays schedule days
     * @param startDate start schedule date
     *
     * @return filtered schedule days
     */
    fun filterActualScheduleBySettings(
        scheduleSettings: ScheduleSettingsSchedule,
        scheduleDays: ArrayList<ScheduleDay>,
        startDate: String,
    ): ArrayList<ScheduleDay>

    /**
     * Get schedule with actual dates.
     *
     * Updating dates on each schedule day
     *
     * @param scheduleSettings schedule settings where specified past days amount
     * @param scheduleDays schedule days
     *
     * @return filtered schedule days
     */
    fun filterScheduleDatesBySettings(
        scheduleSettings: ScheduleSettingsSchedule,
        scheduleDays: ArrayList<ScheduleDay>,
    ): ArrayList<ScheduleDay>

    fun filterScheduleSubgroupBySettings(
        scheduleSettings: ScheduleSettings,
        scheduleDays: ArrayList<ScheduleDay>,
    ): ArrayList<ScheduleDay>

    fun getStartDate(
        scheduleDays: ArrayList<ScheduleDay>
    ): String?

    fun getEndDate(
        scheduleDays: ArrayList<ScheduleDay>
    ): String?

    fun getExamsStartDate(
        examsDays: ArrayList<ScheduleDay>
    ): String?

    fun getExamsEndDate(
        examsDays: ArrayList<ScheduleDay>
    ): String?

}


