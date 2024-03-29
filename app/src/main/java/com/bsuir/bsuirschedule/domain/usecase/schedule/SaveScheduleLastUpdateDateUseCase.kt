package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import java.util.Date

class SaveScheduleLastUpdateDateUseCase(
    private val scheduleRepository: ScheduleRepository
) {

    suspend fun execute(scheduleId: Int, lastUpdateTime: Long, lastUpdateDate: String?): Resource<Unit> {
        val scheduleResult = scheduleRepository.getScheduleById(scheduleId)

        if (scheduleResult is Resource.Error) {
            return Resource.Error(
                statusCode = scheduleResult.statusCode
            )
        }

        val schedule = scheduleResult.data!!
        schedule.lastUpdateTime = lastUpdateTime
        schedule.lastOriginalUpdateTime = Date().time

        val isSavedSchedule = scheduleRepository.saveSchedule(schedule)

        if (isSavedSchedule is Resource.Error) {
            return Resource.Error(
                statusCode = isSavedSchedule.statusCode
            )
        }

        return Resource.Success(null)
    }

}