package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.CurrentWeek
import com.bsuir.bsuirschedule.domain.repository.CurrentWeekRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.WeekManager

class GetCurrentWeekUseCase(private val currentWeekRepository: CurrentWeekRepository) {

    suspend fun getCurrentWeekAPI(): Resource<CurrentWeek> {
        return currentWeekRepository.getCurrentWeekAPI()
    }

    suspend fun updateWeekNumber(): Resource<CurrentWeek> {
        val currentWeekNum = getCurrentWeekAPI()

        if (currentWeekNum is Resource.Success) {
            saveCurrentWeek(currentWeekNum.data!!)
        }

        return currentWeekNum
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
                    val gotWeek = weekManager.getCurrentWeek(initWeek.data!!)
                    Resource.Success(gotWeek)
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


