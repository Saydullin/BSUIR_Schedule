package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class SaveScheduleLastUpdateDateUseCase(
    private val scheduleRepository: ScheduleRepository
) {

    suspend fun execute(scheduleId: Int, lastUpdateTime: Long, lastUpdateDate: String): Resource<Unit> {
        val scheduleResult = scheduleRepository.getScheduleById(scheduleId)

        if (scheduleResult is Resource.Error) {
            return Resource.Error(
                errorType = scheduleResult.errorType
            )
        }

        val schedule = scheduleResult.data!!
        schedule.lastUpdateTime = lastUpdateTime
        schedule.lastUpdateDate = lastUpdateDate

        val isSavedSchedule = scheduleRepository.saveSchedule(schedule)

        if (isSavedSchedule is Resource.Error) {
            return Resource.Error(
                errorType = isSavedSchedule.errorType
            )
        }

        return Resource.Success(null)
    }

}