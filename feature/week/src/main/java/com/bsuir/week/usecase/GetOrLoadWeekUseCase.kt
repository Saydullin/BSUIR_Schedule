package com.bsuir.week.usecase

import com.bsuir.domain.model.week.Week
import com.bsuir.domain.repository.week.WeekDatabaseRepository
import com.bsuir.domain.repository.week.WeekServerRepository
import com.bsuir.resource.Resource
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