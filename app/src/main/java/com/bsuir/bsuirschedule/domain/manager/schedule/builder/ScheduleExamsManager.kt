package com.bsuir.bsuirschedule.domain.manager.schedule.builder

import com.bsuir.bsuirschedule.domain.manager.schedule.contract.ScheduleExamsManagerContract
import com.bsuir.bsuirschedule.domain.models.ScheduleDay

class ScheduleExamsManager : ScheduleExamsManagerContract {

    override fun mergeScheduleAndExams(
        scheduleDays: ArrayList<ScheduleDay>,
        examsDays: ArrayList<ScheduleDay>,
        weekNumber: Int
    ): ArrayList<ScheduleDay> {
        TODO("Not yet implemented")
    }

    override fun setExamsBreakTime(
        examsDays: ArrayList<ScheduleDay>
    ): ArrayList<ScheduleDay> {
        val scheduleBreakTime = ScheduleBreakTime(
            scheduleDays = examsDays
        )

        return scheduleBreakTime.execute()
    }

}


