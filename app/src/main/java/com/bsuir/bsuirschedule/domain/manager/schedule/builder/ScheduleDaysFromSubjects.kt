package com.bsuir.bsuirschedule.domain.manager.schedule.builder

import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject

class ScheduleDaysFromSubjects(
    private val scheduleSubjects: ArrayList<ScheduleSubject>,
    private val currentWeekNumber: Int,
) {

    fun execute(): ArrayList<ScheduleDay> {
        TODO("RETURN SCHEDULE DAY FROM SUBJECTS LIST")
    }

}