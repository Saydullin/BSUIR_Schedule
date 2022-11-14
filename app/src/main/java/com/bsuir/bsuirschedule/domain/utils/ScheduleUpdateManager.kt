package com.bsuir.bsuirschedule.domain.utils

import android.util.Log
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.usecase.GetSavedScheduleUseCase
import com.bsuir.bsuirschedule.domain.usecase.GetScheduleLastUpdateUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.GetScheduleUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.SaveScheduleLastUpdateDateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

class ScheduleUpdateManager(
    private val getScheduleLastUpdateUseCase: GetScheduleLastUpdateUseCase,
    private val getSavedScheduleUseCase: GetSavedScheduleUseCase,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val saveSavedScheduleUseCase: GetSavedScheduleUseCase,
    private val saveScheduleLastUpdateDateUseCase: SaveScheduleLastUpdateDateUseCase
) {

    fun updatedSchedules(): ArrayList<SavedSchedule> {
        return runBlocking {
            val updatedSchedules = ArrayList<SavedSchedule>()
            launch(Dispatchers.IO) {
                when (
                    val result = getShouldUpdateSchedules()
                ) {
                    is Resource.Success -> {
                        val shouldUpdateSchedules = result.data!!
                        updatedSchedules.addAll(updateSchedules(shouldUpdateSchedules))
                    }
                    is Resource.Error -> {
                        // Save in update schedule history table
                        Log.e("sady", "Ops updated ${result.message}")
                    }
                }
            }
            updatedSchedules
        }
    }

    private suspend fun updateSchedules(schedules: ArrayList<SavedSchedule>): ArrayList<SavedSchedule> {
        schedules.map { savedSchedule ->
            if (savedSchedule.lastUpdateDate.isEmpty()) return@map
            when (
                if (savedSchedule.isGroup) {
                    getScheduleUseCase.getGroupAPI(savedSchedule.group.name)
                } else {
                    getScheduleUseCase.getEmployeeAPI(savedSchedule.employee.urlId)
                }
            ) {
                is Resource.Success -> {
                    savedSchedule.isUpdatedSuccessfully = true
                    savedSchedule.lastUpdateTime = Date().time
                    saveScheduleLastUpdateDateUseCase.execute(
                        scheduleId = savedSchedule.id,
                        lastUpdateTime = savedSchedule.lastUpdateTime,
                        lastUpdateDate = savedSchedule.lastUpdateDate,
                    )
                }
                is Resource.Error -> {
                    savedSchedule.isUpdatedSuccessfully = false
                }
            }
        }
        saveSavedScheduleUseCase.saveSchedulesList(schedules)

        return schedules
    }

    private suspend fun getShouldUpdateSchedules(): Resource<ArrayList<SavedSchedule>> {
        val shouldUpdateSavedSchedule = ArrayList<SavedSchedule>()

        when (
            val savedSchedules = getSavedScheduleUseCase.getSavedSchedules()
        ) {
            is Resource.Success -> {
                val savedScheduleList = savedSchedules.data!!
                if (savedScheduleList.isEmpty()) {
                    Log.e("sady", "there is savedSchedules")
                }
                savedScheduleList.map { savedSchedule ->
                    when (
                        val lastUpdatedDate = if (savedSchedule.isGroup) {
                            getScheduleLastUpdateUseCase.getGroupLastUpdateDateByID(savedSchedule.id)
                        } else {
                            getScheduleLastUpdateUseCase.getEmployeeLastUpdateDateByID(savedSchedule.id)
                        }
                    ) {
                        is Resource.Success -> {
                            val lastUpdate = lastUpdatedDate.data!!
                            if ((lastUpdate.lastUpdateDate ?: "") != savedSchedule.lastUpdateDate) {
                                savedSchedule.lastUpdateDate = lastUpdate.lastUpdateDate ?: ""
                                Log.e("sady", "yes updated for ${savedSchedule.id}")
                                shouldUpdateSavedSchedule.add(savedSchedule)
                            } else {
                                Log.e("sady", "no updated for ${savedSchedule.id}")
                            }
                        }
                        is Resource.Error -> {
//                            savedSchedule.isUpdatedSuccessfully = false
                            // FIXME What to do, if server returns null on last update date request
                        }
                    }
                }
            }
            is Resource.Error -> {
                Log.e("sady", "error, u known lastUpdatedDate.message")
                return Resource.Error(
                    errorType = Resource.SERVER_ERROR
                )
            }
        }

        return Resource.Success(
            data = shouldUpdateSavedSchedule
        )

    }

}


