package by.devsgroup.week.usecase

import by.devsgroup.domain.model.week.Week
import by.devsgroup.domain.repository.week.WeekDatabaseRepository
import by.devsgroup.domain.repository.week.WeekServerRepository
import by.devsgroup.resource.Resource
import javax.inject.Inject

class GetOrLoadWeekUseCase @Inject constructor(
    private val weekDatabaseRepository: WeekDatabaseRepository,
    private val weekServerRepository: WeekServerRepository,
) {

    suspend fun execute(): Resource<Week?> {
        return Resource.tryWithSuspend {
            val weekDatabase = weekDatabaseRepository.getCurrentWeek().getOrNull()

            weekDatabase ?: weekServerRepository.getCurrentWeek().getOrNull()
        }
    }

}