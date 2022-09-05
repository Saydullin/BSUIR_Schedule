package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.ScheduleManager

class FullScheduleUseCase {

    fun getSchedule(groupSchedule: GroupSchedule, currentWeek: Int): Resource<Schedule> {
        val sm = ScheduleManager()

        if (groupSchedule.isNotSuitable()) {
            return Resource.Success(groupSchedule.toSchedule())
        }

        return try {
            // Get schedule days as list, instead "monday, tuesday, wednesday" ...
            val schedule = sm.getScheduleModel(groupSchedule)
            // Get schedule for all weeks
            val fullSchedule = sm.getFullScheduleModel(schedule)
//            schedule.schedules = sm.getFullSchedule(schedule, 2)
            // Get break time for each subject (except the first subjects for each day)
            fullSchedule.schedules = sm.getSubjectsBreakTime(fullSchedule.schedules)

            Resource.Success(fullSchedule)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.DATA_ERROR,
                message = e.message
            )
        }
    }

    fun mergeGroupsSubjects(groupSchedule: GroupSchedule, groupItems: ArrayList<Group>) {
        val sm = ScheduleManager()
        sm.mergeGroupsSubjects(groupSchedule, groupItems)
    }

}