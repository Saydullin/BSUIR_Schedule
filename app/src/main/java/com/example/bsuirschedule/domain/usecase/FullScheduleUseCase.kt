package com.example.bsuirschedule.domain.usecase

import android.util.Log
import com.example.bsuirschedule.domain.models.Group
import com.example.bsuirschedule.domain.models.GroupSchedule
import com.example.bsuirschedule.domain.models.Schedule
import com.example.bsuirschedule.domain.utils.Resource
import com.example.bsuirschedule.domain.utils.ScheduleManager

class FullScheduleUseCase {

    fun getSchedule(groupSchedule: GroupSchedule): Resource<Schedule> {
        val sm = ScheduleManager()

        if (groupSchedule.isNotSuitable()) {
            return Resource.Success(Schedule.empty)
        }

        return try {
            // Get schedule days as list, instead "monday, tuesday, wednesday" ...
            val schedule = sm.getScheduleModel(groupSchedule)
            // Get schedule for all weeks
            val fullSchedule = sm.getFullScheduleModel(schedule)
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