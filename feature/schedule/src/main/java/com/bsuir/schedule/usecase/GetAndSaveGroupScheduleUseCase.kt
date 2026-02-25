package com.bsuir.schedule.usecase

import com.bsuir.domain.model.schedule.common.ScheduleType
import com.bsuir.domain.repository.schedule.ScheduleDatabaseRepository
import com.bsuir.domain.repository.schedule.ScheduleServerRepository
import com.bsuir.resource.Resource
import com.bsuir.schedule.manager.ScheduleManager
import javax.inject.Inject

class GetAndSaveGroupScheduleUseCase @Inject constructor(
    private val scheduleServerRepository: ScheduleServerRepository,
    private val scheduleDatabaseRepository: ScheduleDatabaseRepository,
) {

    suspend fun execute(
        currentWeek: Int,
        groupName: String
    ): Resource<Unit> {
        return Resource.tryWithSuspend {
            val groupSchedule = scheduleServerRepository.getGroupSchedule(groupName)
                .getOrThrow()

            val scheduleManager = ScheduleManager(
                currentWeek = currentWeek,
                scheduleType = ScheduleType.GROUP,
                scheduleTemplate = groupSchedule
            )

            val fullSchedule = scheduleManager.getFullSchedule()

            scheduleDatabaseRepository.saveSchedule(fullSchedule)
        }
    }

}


