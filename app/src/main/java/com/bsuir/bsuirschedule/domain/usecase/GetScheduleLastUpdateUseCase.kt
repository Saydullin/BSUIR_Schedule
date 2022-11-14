package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.ScheduleLastUpdatedDate
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class GetScheduleLastUpdateUseCase(
    private val scheduleRepository: ScheduleRepository
) {

    suspend fun getGroupLastUpdateDateByID(scheduleId: Int): Resource<ScheduleLastUpdatedDate> {
        return scheduleRepository.getGroupLastUpdatedDate(scheduleId)
    }

    suspend fun getEmployeeLastUpdateDateByID(scheduleId: Int): Resource<ScheduleLastUpdatedDate> {
        return scheduleRepository.getEmployeeLastUpdatedDate(scheduleId)
    }

}


