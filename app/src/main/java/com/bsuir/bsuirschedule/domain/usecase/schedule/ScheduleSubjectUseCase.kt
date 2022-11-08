package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.models.DeleteSubjectSettings
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

    suspend fun delete(scheduleId: Int, scheduleSubject: ScheduleSubject, deleteSettings: DeleteSubjectSettings): Resource<Schedule> {
        return when (
            val result = scheduleRepository.getScheduleById(scheduleId)
        ) {
            is Resource.Success -> {
                val data = result.data!!
                data.schedules = getDeletedSubjectSchedule(data.schedules, scheduleSubject)
                if (deleteSettings.deleteAll) {
                    data.schedules = getDeletedAllSubjectsSchedule(data.schedules, scheduleSubject)
                }
                if (deleteSettings.deleteOnlyType) {
                    data.schedules = getDeletedTypeSubjectsSchedule(data.schedules, scheduleSubject)
                }
                if (deleteSettings.deleteOnlyPeriod) {
                    data.schedules = getDeletedPeriodSubjectsSchedule(data.schedules, scheduleSubject)
                }
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

    private fun getDeletedPeriodSubjectsSchedule(
        scheduleDaysList: ArrayList<ScheduleDay>,
        scheduleSubject: ScheduleSubject
    ): ArrayList<ScheduleDay> {
        for (day in scheduleDaysList) {
            day.schedule = day.schedule.filterNot { subject ->
                (subject.subject == scheduleSubject.subject &&
                        subject.lessonTypeAbbrev == scheduleSubject.lessonTypeAbbrev &&
                        subject.dayNumber == scheduleSubject.dayNumber &&
                        subject.weekNumber == scheduleSubject.weekNumber)
            } as ArrayList<ScheduleSubject>
        }

        return scheduleDaysList
    }

    private fun getDeletedTypeSubjectsSchedule(
        scheduleDaysList: ArrayList<ScheduleDay>,
        scheduleSubject: ScheduleSubject
    ): ArrayList<ScheduleDay> {
        for (day in scheduleDaysList) {
            day.schedule = day.schedule.filterNot { subject ->
                (subject.subject == scheduleSubject.subject &&
                        subject.lessonTypeAbbrev == scheduleSubject.lessonTypeAbbrev)
            } as ArrayList<ScheduleSubject>
        }

        return scheduleDaysList
    }

    private fun getDeletedAllSubjectsSchedule(
        scheduleDaysList: ArrayList<ScheduleDay>,
        scheduleSubject: ScheduleSubject
    ): ArrayList<ScheduleDay> {
        for (day in scheduleDaysList) {
            day.schedule = day.schedule.filterNot { subject ->
                (subject.subject == scheduleSubject.subject)
            } as ArrayList<ScheduleSubject>
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


