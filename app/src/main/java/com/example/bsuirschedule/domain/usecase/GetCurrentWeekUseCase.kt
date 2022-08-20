package com.example.bsuirschedule.domain.usecase

import com.example.bsuirschedule.domain.models.CurrentWeek
import com.example.bsuirschedule.domain.repository.CurrentWeekRepository
import com.example.bsuirschedule.domain.utils.Resource

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