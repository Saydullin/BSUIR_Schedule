package com.bsuir.bsuirschedule.domain.manager.schedule.builder

import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.utils.Resource

class ScheduleFromGroupSchedule(
    groupSchedule: GroupSchedule,
    currentWeekNumber: Int,
) {

    fun execute(): Resource<Schedule> {
        TODO("DO SOMETHING ABOUT IT")
    }

}


