package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.CurrentWeek
import com.bsuir.bsuirschedule.domain.repository.CurrentWeekRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class GetCurrentWeekUseCase(private val currentWeekRepository: CurrentWeekRepository) {

    suspend fun getCurrentWeekAPI(): Resource<CurrentWeek> {
        return currentWeekRepository.getCurrentWeekAPI()
    }

    suspend fun getCurrentWeek(): Resource<CurrentWeek> {
        // TODO: Calculate current week by got data
        return currentWeekRepository.getCurrentWeek()
    }

    suspend fun saveCurrentWeek(currentWeek: CurrentWeek): Resource<Unit> {
        return currentWeekRepository.saveCurrentWeek(currentWeek)
    }

}