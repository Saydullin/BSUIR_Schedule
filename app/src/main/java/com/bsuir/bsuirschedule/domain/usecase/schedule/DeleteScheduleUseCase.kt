package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class DeleteScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
) {

    suspend fun invoke(savedSchedule: SavedSchedule): Resource<Unit> {
        return scheduleRepository.deleteScheduleById(savedSchedule.id)
    }

}


