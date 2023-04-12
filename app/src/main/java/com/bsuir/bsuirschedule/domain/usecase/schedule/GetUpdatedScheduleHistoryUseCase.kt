package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.models.ScheduleUpdatedAction
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.StatusCode

class GetUpdatedScheduleHistoryUseCase(
    private val scheduleRepository: ScheduleRepository,
) {

    suspend fun invoke(scheduleId: Int): Resource<ArrayList<ScheduleUpdatedAction>> {
        val scheduleUpdatedActions = ArrayList<ScheduleUpdatedAction>()
        val scheduleResult = scheduleRepository.getScheduleById(scheduleId)

        if (scheduleResult is Resource.Success && scheduleResult.data != null) {
            val schedule = scheduleResult.data


        } else {
            return Resource.Error(
                statusCode = StatusCode.DATA_ERROR
            )
        }

        return Resource.Success(scheduleUpdatedActions)
    }

}