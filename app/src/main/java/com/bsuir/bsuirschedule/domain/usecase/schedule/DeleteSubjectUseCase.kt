package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.models.ChangeSubjectSettings
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleTerm
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class DeleteSubjectUseCase(
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
            scheduleDays = getDeletedAllSubjectsSchedule(
                scheduleDays = scheduleDays,
                scheduleSubject = scheduleSubject,
                isOnlySubgroup = deleteSettings.forOnlySubgroup
            )
        } else if (deleteSettings.forOnlyType || deleteSettings.forOnlyPeriod) {
            if (deleteSettings.forOnlyType) {
                scheduleDays = getDeletedTypeSubjectsSchedule(
                    scheduleDays = scheduleDays,
                    scheduleSubject = scheduleSubject,
                    isOnlySubgroup = deleteSettings.forOnlySubgroup
                )
            }
            if (deleteSettings.forOnlyPeriod) {
                scheduleDays = getDeletedPeriodSubjectsSchedule(
                    scheduleDays = scheduleDays,
                    scheduleSubject = scheduleSubject,
                    isOnlySubgroup = deleteSettings.forOnlySubgroup
                )
            }
        } else {
            scheduleDays = getDeletedSubjectSchedule(
                scheduleDays = scheduleDays,
                scheduleSubject = scheduleSubject
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

    private fun getDeletedPeriodSubjectsSchedule(
        scheduleDays: ArrayList<ScheduleDay>,
        scheduleSubject: ScheduleSubject,
        isOnlySubgroup: Boolean,
    ): ArrayList<ScheduleDay> {
        for (day in scheduleDays) {
            day.schedule = day.schedule.filterNot { subject ->
                (subject.getEditedOrShortTitle() == scheduleSubject.getEditedOrShortTitle() &&
                        subject.getEditedOrLessonType() == scheduleSubject.getEditedOrLessonType() &&
                        ((isOnlySubgroup && subject.getEditedOrNumSubgroup() == scheduleSubject.getEditedOrNumSubgroup()) || !isOnlySubgroup) &&
                        subject.getEditedOrWeekDay() == scheduleSubject.getEditedOrWeekDay() &&
                        subject.getEditedOrWeeks() == scheduleSubject.getEditedOrWeeks())
            } as ArrayList<ScheduleSubject>
        }

        return scheduleDays
    }

    private fun getDeletedTypeSubjectsSchedule(
        scheduleDays: ArrayList<ScheduleDay>,
        scheduleSubject: ScheduleSubject,
        isOnlySubgroup: Boolean,
    ): ArrayList<ScheduleDay> {
        for (day in scheduleDays) {
            day.schedule = day.schedule.filterNot { subject ->
                (subject.getEditedOrShortTitle() == scheduleSubject.getEditedOrShortTitle() &&
                        ((isOnlySubgroup && subject.getEditedOrNumSubgroup() == scheduleSubject.getEditedOrNumSubgroup()) || !isOnlySubgroup) &&
                        subject.getEditedOrLessonType() == scheduleSubject.getEditedOrLessonType())
            } as ArrayList<ScheduleSubject>
        }

        return scheduleDays
    }

    private fun getDeletedAllSubjectsSchedule(
        scheduleDays: ArrayList<ScheduleDay>,
        scheduleSubject: ScheduleSubject,
        isOnlySubgroup: Boolean,
    ): ArrayList<ScheduleDay> {
        for (day in scheduleDays) {
            day.schedule = day.schedule.filterNot { subject ->
                (subject.getEditedOrShortTitle() == scheduleSubject.getEditedOrShortTitle() &&
                        ((isOnlySubgroup && subject.getEditedOrNumSubgroup() == scheduleSubject.getEditedOrNumSubgroup()) || !isOnlySubgroup))
            } as ArrayList<ScheduleSubject>
        }

        return scheduleDays
    }

    private fun getDeletedSubjectSchedule(
        scheduleDays: ArrayList<ScheduleDay>,
        scheduleSubject: ScheduleSubject
    ): ArrayList<ScheduleDay> {
        for (day in scheduleDays) {
            val index = day.schedule.indexOfFirst { it.id == scheduleSubject.id }

            if (index != -1) {
                day.schedule.removeAt(index)
                break
            }
        }

        return scheduleDays
    }

}


