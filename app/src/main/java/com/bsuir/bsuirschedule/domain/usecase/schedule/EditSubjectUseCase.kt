package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.models.ChangeSubjectSettings
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleTerm
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class EditSubjectUseCase(
    private val scheduleRepository: ScheduleRepository,
) {

    suspend fun execute(
        scheduleId: Int,
        scheduleSubject: ScheduleSubject,
        deleteSettings: ChangeSubjectSettings,
        scheduleTerm: ScheduleTerm = ScheduleTerm.CURRENT_SCHEDULE
    ): Resource<Schedule> {
        val scheduleRes = scheduleRepository.getScheduleById(scheduleId)

        if (scheduleRes is Resource.Error || scheduleRes.data == null) {
            return scheduleRes
        }

        val schedule = scheduleRes.data
        var scheduleDays = ArrayList<ScheduleDay>()

        when(scheduleTerm) {
            ScheduleTerm.CURRENT_SCHEDULE -> {
                scheduleDays = schedule.schedules
            }
            ScheduleTerm.PREVIOUS_SCHEDULE -> {
                scheduleDays = schedule.previousSchedules
            }
            ScheduleTerm.SESSION -> {
                scheduleDays = schedule.examsSchedule
            }
            else -> {}
        }

        if (deleteSettings.forAll) {
            scheduleDays = getEditedAllSubjectsSchedule(
                scheduleDays = scheduleDays,
                scheduleSubject = scheduleSubject,
                isOnlySubgroup = deleteSettings.forOnlySubgroup
            )
        } else if (deleteSettings.forOnlyType || deleteSettings.forOnlyPeriod) {
            if (deleteSettings.forOnlyType) {
                scheduleDays = getEditedTypeSubjectsSchedule(
                    scheduleDays = scheduleDays,
                    scheduleSubject = scheduleSubject,
                    isOnlySubgroup = deleteSettings.forOnlySubgroup
                )
            }
            if (deleteSettings.forOnlyPeriod) {
                scheduleDays = getEditedPeriodSubjectsSchedule(
                    scheduleDays = scheduleDays,
                    scheduleSubject = scheduleSubject,
                    isOnlySubgroup = deleteSettings.forOnlySubgroup
                )
            }
        } else {
            scheduleDays = getEditedSubjectSchedule(
                scheduleDays = scheduleDays,
                scheduleSubject = scheduleSubject,
            )
        }

        when(scheduleTerm) {
            ScheduleTerm.CURRENT_SCHEDULE -> {
                schedule.schedules = scheduleDays
            }
            ScheduleTerm.PREVIOUS_SCHEDULE -> {
                schedule.previousSchedules = scheduleDays
            }
            ScheduleTerm.SESSION -> {
                schedule.examsSchedule = scheduleDays
            }
            else -> {}
        }

        return Resource.Success(schedule)
    }

    private fun getEditedAllSubjectsSchedule(
        scheduleDays: ArrayList<ScheduleDay>,
        scheduleSubject: ScheduleSubject,
        isOnlySubgroup: Boolean
    ): ArrayList<ScheduleDay> {
        for (day in scheduleDays) {
            day.schedule.map { subject ->
                if (subject.subject == scheduleSubject.subject &&
                    ((isOnlySubgroup && subject.numSubgroup == scheduleSubject.numSubgroup) || !isOnlySubgroup)) {

                    subject.edited = scheduleSubject.edited
                }
            }
        }

        return scheduleDays
    }

    private fun getEditedTypeSubjectsSchedule(
        scheduleDays: ArrayList<ScheduleDay>,
        scheduleSubject: ScheduleSubject,
        isOnlySubgroup: Boolean
    ): ArrayList<ScheduleDay> {
        for (day in scheduleDays) {
            day.schedule.map { subject ->
                if (subject.subject == scheduleSubject.subject &&
                    ((isOnlySubgroup && subject.numSubgroup == scheduleSubject.numSubgroup) || !isOnlySubgroup) &&
                    subject.lessonTypeAbbrev == scheduleSubject.lessonTypeAbbrev) {
                    subject.edited = scheduleSubject.edited
                }
            }
        }

        return scheduleDays
    }

    private fun getEditedPeriodSubjectsSchedule(
        scheduleDays: ArrayList<ScheduleDay>,
        scheduleSubject: ScheduleSubject,
        isOnlySubgroup: Boolean
    ): ArrayList<ScheduleDay> {
        for (day in scheduleDays) {
            day.schedule.map { subject ->
                if (
                    subject.subject == scheduleSubject.subject &&
                    ((isOnlySubgroup && subject.numSubgroup == scheduleSubject.numSubgroup) || !isOnlySubgroup) &&
                    subject.lessonTypeAbbrev == scheduleSubject.lessonTypeAbbrev &&
                    subject.dayNumber == scheduleSubject.dayNumber &&
                    subject.weekNumber == scheduleSubject.weekNumber
                ) {
                    subject.edited = scheduleSubject.edited
                }
            }
        }

        return scheduleDays
    }

    private fun getEditedSubjectSchedule(
        scheduleDays: ArrayList<ScheduleDay>,
        scheduleSubject: ScheduleSubject,
    ): ArrayList<ScheduleDay> {
        for (day in scheduleDays) {
            val index = day.schedule.indexOfFirst { it.id == scheduleSubject.id }

            if (index != -1) {
                day.schedule[index] = scheduleSubject
                break
            }
        }

        return scheduleDays
    }

}


