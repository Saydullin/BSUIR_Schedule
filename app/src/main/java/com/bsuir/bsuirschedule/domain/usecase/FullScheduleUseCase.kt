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
            val fullSchedule = sm.getFullScheduleModel(schedule, currentWeek)
            // Get schedule for selected subgroup
            fullSchedule.schedules = sm.filterBySubgroup(fullSchedule.schedules, fullSchedule.selectedSubgroup)
            // Get break time for each subject (except the first subjects for each day)
            fullSchedule.schedules = sm.getSubjectsBreakTime(fullSchedule.schedules)
            // Set Current Subject
            sm.setCurrentSubject(fullSchedule)

            Resource.Success(fullSchedule)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.DATA_ERROR,
                message = e.message
            )
        }
    }

    fun getSchedule2(groupSchedule: GroupSchedule, currentWeek: Int): Resource<Schedule> {
        val sm = ScheduleManager()

        if (groupSchedule.isNotSuitable()) {
            return Resource.Success(groupSchedule.toSchedule())
        }

        return try {
            // Get schedule days as list, instead "monday, tuesday, wednesday" ...
            val schedule = sm.getScheduleModel(groupSchedule)
            // Get schedule for all weeks
            val fullSchedule = sm.getFullScheduleModel(schedule, currentWeek)
            // Get schedule for selected subgroup
            fullSchedule.schedules = sm.filterBySubgroup(fullSchedule.schedules, fullSchedule.selectedSubgroup)
            // Get break time for each subject (except the first subjects for each day)
            fullSchedule.schedules = sm.getSubjectsBreakTime(fullSchedule.schedules)
            // Set Current Subject
            sm.setCurrentSubject(fullSchedule)

            Resource.Success(fullSchedule)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.DATA_ERROR,
                message = e.message
            )
        }
    }

    fun mergeGroupsSubjects(schedule: Schedule, groupItems: ArrayList<Group>) {
        val sm = ScheduleManager()
        sm.mergeGroupsSubjects(schedule, groupItems)
    }

}


