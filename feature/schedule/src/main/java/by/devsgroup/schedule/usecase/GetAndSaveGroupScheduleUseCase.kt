package by.devsgroup.schedule.usecase

import by.devsgroup.domain.repository.schedule.ScheduleDatabaseRepository
import by.devsgroup.domain.repository.schedule.ScheduleServerRepository
import by.devsgroup.resource.Resource
import by.devsgroup.schedule.manager.ScheduleManager
import javax.inject.Inject

class GetAndSaveGroupScheduleUseCase @Inject constructor(
    private val scheduleServerRepository: ScheduleServerRepository,
    private val scheduleDatabaseRepository: ScheduleDatabaseRepository,
) {

    suspend fun execute(groupName: String): Resource<Unit> {
        return Resource.tryWithSuspend {
            val groupSchedule = scheduleServerRepository.getGroupSchedule(groupName)
                .getOrThrow()

            val scheduleManager = ScheduleManager(
                currentWeek = 1,
                scheduleTemplate = groupSchedule
            )

            val fullSchedule = scheduleManager.getFullSchedule()

            scheduleDatabaseRepository.saveSchedule(fullSchedule)
        }
    }

}


