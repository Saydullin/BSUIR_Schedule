package com.bsuir.bsuirschedule.domain.usecase.schedule

import android.util.Log
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class ScheduleSubjectUseCase(
    private val scheduleRepository: ScheduleRepository,
) {

    suspend fun ignore(scheduleId: Int, scheduleSubject: ScheduleSubject, isIgnored: Boolean = true): Resource<Schedule> {
        return when (
            val result = scheduleRepository.getScheduleById(scheduleId)
        ) {
            is Resource.Success -> {
                val data = result.data!!
                data.schedules = getIgnoredSubjectSchedule(data.schedules, scheduleSubject, isIgnored)
                return Resource.Success(data)
            }
            is Resource.Error -> {
                Resource.Error(
                    errorType = result.errorType,
                    message = result.message
                )
            }
        }
    }

    suspend fun delete(scheduleId: Int, scheduleSubject: ScheduleSubject): Resource<Schedule> {
        return when (
            val result = scheduleRepository.getScheduleById(scheduleId)
        ) {
            is Resource.Success -> {
                val data = result.data!!
                data.schedules = getDeletedSubjectSchedule(data.schedules, scheduleSubject)
                return Resource.Success(data)
            }
            is Resource.Error -> {
                Resource.Error(
                    errorType = result.errorType,
                    message = result.message
                )
            }
        }
    }

    private fun getIgnoredSubjectSchedule(
        scheduleDaysList: ArrayList<ScheduleDay>,
        scheduleSubject: ScheduleSubject,
        isIgnored: Boolean
    ): ArrayList<ScheduleDay> {
        for (day in scheduleDaysList) {
            val index = day.schedule.indexOfFirst { it.id == scheduleSubject.id }

            if (index != -1) {
                day.schedule[index].isIgnored = isIgnored
                break
            }
        }

        return scheduleDaysList
    }

    private fun getDeletedSubjectSchedule(
        scheduleDaysList: ArrayList<ScheduleDay>,
        scheduleSubject: ScheduleSubject
    ): ArrayList<ScheduleDay> {
        for (day in scheduleDaysList) {
            val index = day.schedule.indexOfFirst { it.id == scheduleSubject.id }

            if (index != -1) {
                day.schedule.removeAt(index)
                break
            }
        }

        return scheduleDaysList
    }

}


