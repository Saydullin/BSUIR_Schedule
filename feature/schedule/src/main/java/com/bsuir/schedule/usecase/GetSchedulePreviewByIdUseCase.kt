package com.bsuir.schedule.usecase

import com.bsuir.domain.model.schedule.preview.PreviewSchedule
import com.bsuir.domain.repository.schedule.ScheduleDatabaseRepository
import com.bsuir.resource.Resource
import javax.inject.Inject

class GetSchedulePreviewByIdUseCase @Inject constructor(
    private val scheduleDatabaseRepository: ScheduleDatabaseRepository
) {

    suspend fun execute(scheduleId: Long): Resource<PreviewSchedule?> {
        return scheduleDatabaseRepository.getScheduleById(scheduleId)
    }

}