package com.bsuir.bsuirschedule.domain.manager.schedule.contract

import com.bsuir.bsuirschedule.domain.models.Employee
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Holiday
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
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

}


