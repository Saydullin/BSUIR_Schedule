package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleTerm
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class IgnoreSubjectUseCase(
    private val scheduleRepository: ScheduleRepository,
) {

    suspend fun execute(
        scheduleId: Int,
        scheduleSubject: ScheduleSubject,
        scheduleTerm: ScheduleTerm = ScheduleTerm.CURRENT_SCHEDULE,
        isIgnored: Boolean = true
    ): Resource<Schedule> {
        return when (
            val result = scheduleRepository.getScheduleById(scheduleId)
        ) {
            is Resource.Success -> {
                val data = result.data!!
                when(scheduleTerm) {
                    ScheduleTerm.CURRENT_SCHEDULE -> {
                        data.schedules = getIgnoredSubjectSchedule(data.schedules, scheduleSubject, isIgnored)
                    }
                    ScheduleTerm.PREVIOUS_SCHEDULE -> {
                        data.previousSchedules = getIgnoredSubjectSchedule(data.previousSchedules, scheduleSubject, isIgnored)
                    }
                    ScheduleTerm.SESSION -> {
                        data.examsSchedule = getIgnoredSubjectSchedule(data.examsSchedule, scheduleSubject, isIgnored)
                    }
                    else -> {}
                }
                return Resource.Success(data)
            }
            is Resource.Error -> {
                Resource.Error(
                    statusCode = result.statusCode,
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

}


