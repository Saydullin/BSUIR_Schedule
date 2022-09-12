package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.CurrentWeek
import com.bsuir.bsuirschedule.domain.repository.CurrentWeekRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.WeekManager

class GetCurrentWeekUseCase(private val currentWeekRepository: CurrentWeekRepository) {

    suspend fun getCurrentWeekAPI(): Resource<CurrentWeek> {
        return currentWeekRepository.getCurrentWeekAPI()
    }

    suspend fun isCurrentWeekPassed(): Resource<Boolean> {
        val initWeek = currentWeekRepository.getCurrentWeek()
        val weekManager = WeekManager()

        return when (initWeek) {
            is Resource.Success -> {
                try {
                    return Resource.Success(weekManager.isWeekPassed(initWeek.data!!))
                } catch (e: Exception) {
                    return Resource.Error(
                        errorType = Resource.DATA_ERROR,
                        message = e.message
                    )
                }
            }
            is Resource.Error -> {
                Resource.Error(
                    errorType = initWeek.errorType,
                    message = initWeek.message
                )
            }
        }
    }

    suspend fun getCurrentWeek(): Resource<Int> {
        val initWeek = currentWeekRepository.getCurrentWeek()
        val weekManager = WeekManager()

        return try {
            when (initWeek) {
                is Resource.Success -> {
                    Resource.Success(weekManager.getCurrentWeek(initWeek.data!!))
                }
                is Resource.Error -> {
                    Resource.Error(
                        errorType = initWeek.errorType,
                        message = initWeek.message
                    )
                }
            }
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.DATA_ERROR,
                message = e.message
            )
        }
    }

    suspend fun saveCurrentWeek(currentWeek: CurrentWeek): Resource<Unit> {
        return currentWeekRepository.saveCurrentWeek(currentWeek)
    }

}


