package by.devsgroup.schedule.usecase

import by.devsgroup.domain.model.schedule.preview.PreviewSchedule
import by.devsgroup.domain.repository.schedule.ScheduleDatabaseRepository
import by.devsgroup.resource.Resource
import javax.inject.Inject

class GetSchedulePreviewByIdUseCase @Inject constructor(
    private val scheduleDatabaseRepository: ScheduleDatabaseRepository
) {

    suspend fun execute(scheduleId: Long): Resource<PreviewSchedule?> {
        return scheduleDatabaseRepository.getScheduleById(scheduleId)
    }

}