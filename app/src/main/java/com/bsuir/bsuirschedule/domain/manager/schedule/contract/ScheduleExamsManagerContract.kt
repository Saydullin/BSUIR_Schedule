package com.bsuir.bsuirschedule.domain.manager.schedule.contract

import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject

/**
 * Contract for ScheduleExamsManager class
 */
interface ScheduleExamsManagerContract {

    fun mergeScheduleAndExams(
        scheduleDays: ArrayList<ScheduleDay>,
        examsDays: ArrayList<ScheduleDay>,
        weekNumber: Int
    ): ArrayList<ScheduleDay>

    fun setExamsBreakTime(
        examsDays: ArrayList<ScheduleDay>,
    ): ArrayList<ScheduleDay>

}


