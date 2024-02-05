package com.bsuir.bsuirschedule.domain.utils

import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.usecase.GetSavedScheduleUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.GetScheduleUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.SaveScheduleUseCase
import java.util.Date

class ScheduleUpdateManager(
    private val getSavedScheduleUseCase: GetSavedScheduleUseCase,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val saveScheduleUseCase: SaveScheduleUseCase,
) {

    suspend fun execute(): ArrayList<SavedSchedule> {
        val updatedSchedules = ArrayList<SavedSchedule>()
        val schedulesToBeUpdated = getSchedulesToBeUpdated()

        schedulesToBeUpdated.map { savedSchedule ->
            val isUpdatedSuccessfully = updateSchedule(savedSchedule)
            if (isUpdatedSuccessfully) {
                updatedSchedules.add(savedSchedule)
            }
        }

        return updatedSchedules
    }

    private suspend fun getSchedulesToBeUpdated(): ArrayList<SavedSchedule> {
        val schedulesToBeUpdated = ArrayList<SavedSchedule>()
        val savedScheduleResult = getSavedScheduleUseCase.getSavedSchedules()

        if (savedScheduleResult is Resource.Success && savedScheduleResult.data != null) {
            val savedSchedules = savedScheduleResult.data
            savedSchedules.map { savedSchedule ->
                val isAutoUpdateEnabled = isAutoUpdateEnabled(savedSchedule)
                if (isAutoUpdateEnabled) {
                    val isHaveUpdates = isScheduleHaveUpdates(savedSchedule)

                    if (isHaveUpdates) {
                        schedulesToBeUpdated.add(savedSchedule)
                    }
                }
            }
        }

        return schedulesToBeUpdated
    }

    private suspend fun isAutoUpdateEnabled(savedSchedule: SavedSchedule): Boolean {
        val scheduleResult = getScheduleUseCase.getById(savedSchedule.id)

        if (scheduleResult is Resource.Success && scheduleResult.data != null) {
            return scheduleResult.data.settings.schedule.isAutoUpdate
        }

        return false
    }

    private suspend fun isScheduleHaveUpdates(savedSchedule: SavedSchedule): Boolean {
        val scheduleAPIResult = if (savedSchedule.isGroup) {
            getScheduleUseCase.getGroupAPI(savedSchedule.group.name)
        } else {
            savedSchedule.employee.urlId?.let { getScheduleUseCase.getEmployeeAPI(it) }
        }
        val scheduleResult = getScheduleUseCase.getById(savedSchedule.id)

        if (scheduleAPIResult is Resource.Success && scheduleAPIResult.data != null &&
                scheduleResult is Resource.Success && scheduleResult.data != null) {

            return scheduleResult.data.originalSchedule != scheduleAPIResult.data.originalSchedule
        }

        return false
    }

    private suspend fun updateSchedule(savedSchedule: SavedSchedule): Boolean {
        val scheduleResult = if (savedSchedule.isGroup) {
            getScheduleUseCase.getGroupAPI(savedSchedule.group.name)
        } else {
            savedSchedule.employee.urlId?.let { getScheduleUseCase.getEmployeeAPI(it) }
        }

        if (scheduleResult is Resource.Success && scheduleResult.data != null) {
            val schedule = scheduleResult.data

            schedule.lastUpdateTime = Date().time
            schedule.lastOriginalUpdateTime = Date().time

            return saveSchedule(scheduleResult.data)
        }

        return false

    }

    private suspend fun saveSchedule(schedule: Schedule): Boolean {
        val saveScheduleResult = saveScheduleUseCase.execute(schedule)

        return saveScheduleResult is Resource.Success
    }

}


