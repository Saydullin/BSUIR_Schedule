package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class UpdateScheduleSettingsUseCase(
    private val scheduleRepository: ScheduleRepository,
) {

    suspend fun invoke(id: Int, newSchedule: ScheduleSettings): Resource<Unit> {
        return scheduleRepository.updateScheduleSettings(id, newSchedule)
    }

}


