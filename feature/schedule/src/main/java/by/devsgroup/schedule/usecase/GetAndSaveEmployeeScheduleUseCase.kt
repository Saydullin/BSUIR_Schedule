package by.devsgroup.schedule.usecase

import by.devsgroup.domain.repository.schedule.ScheduleDatabaseRepository
import by.devsgroup.domain.repository.schedule.ScheduleServerRepository
import by.devsgroup.domain.repository.week.WeekDatabaseRepository
import by.devsgroup.resource.Resource
import by.devsgroup.schedule.manager.ScheduleManager
import javax.inject.Inject

class GetAndSaveEmployeeScheduleUseCase @Inject constructor(
    private val scheduleServerRepository: ScheduleServerRepository,
    private val scheduleDatabaseRepository: ScheduleDatabaseRepository,
) {

    suspend fun execute(
        currentWeek: Int,
        urlId: String
    ): Resource<Unit> {
        return Resource.tryWithSuspend {
            val employeeSchedule = scheduleServerRepository.getEmployeeSchedule(urlId)
                .getOrThrow()

            val scheduleManager = ScheduleManager(
                currentWeek = currentWeek,
                scheduleTemplate = employeeSchedule
            )

            val fullSchedule = scheduleManager.getFullSchedule()

            scheduleDatabaseRepository.saveSchedule(fullSchedule)
        }
    }

}


