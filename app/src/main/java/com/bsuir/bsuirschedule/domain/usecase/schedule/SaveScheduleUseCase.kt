package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class SaveScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
) {

    suspend fun invoke(schedule: Schedule): Resource<Unit> {
        return scheduleRepository.saveSchedule(schedule)
    }

}


