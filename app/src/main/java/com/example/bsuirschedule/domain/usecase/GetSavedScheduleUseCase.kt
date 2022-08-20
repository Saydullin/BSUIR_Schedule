package com.example.bsuirschedule.domain.usecase

import com.example.bsuirschedule.domain.models.SavedSchedule
import com.example.bsuirschedule.domain.repository.SavedScheduleRepository
import com.example.bsuirschedule.domain.utils.Resource
import kotlin.collections.ArrayList

class GetSavedScheduleUseCase(private val savedScheduleRepository: SavedScheduleRepository) {

    suspend fun getSavedSchedules(): Resource<ArrayList<SavedSchedule>> {
        val result = savedScheduleRepository.getSavedSchedules()
        return try {
            when(result) {
                is Resource.Success -> {
                    Resource.Success(result.data!!)
                }
                is Resource.Error -> {
                    Resource.Error(
                        errorType = result.errorType,
                        message = result.message
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

    suspend fun saveSchedule(schedule: SavedSchedule): Resource<Unit> {
        return savedScheduleRepository.saveSchedule(schedule)
    }

    suspend fun getSavedScheduleById(scheduleId: Int): Resource<SavedSchedule> {
        return savedScheduleRepository.getSavedScheduleById(scheduleId)
    }

    suspend fun filterByKeywordASC(title: String): Resource<ArrayList<SavedSchedule>> {
        val result = savedScheduleRepository.filterByKeywordASC(title)
        return try {
            when(result) {
                is Resource.Success -> {
                    Resource.Success(result.data!!)
                }
                is Resource.Error -> {
                    Resource.Error(
                        errorType = result.errorType,
                        message = result.message
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

    suspend fun filterByKeywordDESC(title: String): Resource<ArrayList<SavedSchedule>> {
        val result = savedScheduleRepository.filterByKeywordDESC(title)
        return try {
            when(result) {
                is Resource.Success -> {
                    Resource.Success(result.data!!)
                }
                is Resource.Error -> {
                    Resource.Error(
                        errorType = result.errorType,
                        message = result.message
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

    suspend fun deleteSchedule(schedule: SavedSchedule): Resource<Unit> {
        return savedScheduleRepository.deleteSchedule(schedule)
    }

}