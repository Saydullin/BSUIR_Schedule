package com.bsuir.bsuirschedule.domain.utils

import android.util.Log
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.models.ScheduleLastUpdatedDate
import com.bsuir.bsuirschedule.domain.usecase.GetSavedScheduleUseCase
import com.bsuir.bsuirschedule.domain.usecase.GetScheduleLastUpdateUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.GetScheduleUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.SaveScheduleLastUpdateDateUseCase
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class ScheduleUpdateManager(
    private val getScheduleLastUpdateUseCase: GetScheduleLastUpdateUseCase,
    private val getSavedScheduleUseCase: GetSavedScheduleUseCase,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val saveSavedScheduleUseCase: GetSavedScheduleUseCase,
    private val saveScheduleLastUpdateDateUseCase: SaveScheduleLastUpdateDateUseCase
) {

    suspend fun updatedSchedules(): ArrayList<SavedSchedule> = withContext(Dispatchers.IO) {
        val updatedSchedules = ArrayList<SavedSchedule>()
        when (
            val result = getShouldUpdateSchedules()
        ) {
            is Resource.Success -> {
                val shouldUpdateSchedules = result.data!!
                updatedSchedules.addAll(updateSchedules(shouldUpdateSchedules))
            }
            is Resource.Error -> {
                // Save error in update schedule history table
                Log.e("sady", "Ops updated ${result.message}")
            }
        }
        updatedSchedules
    }

    private suspend fun getScheduleAutoUpdateStatus(savedSchedule: SavedSchedule): Boolean {
        when (val scheduleDB = getScheduleUseCase.getById(savedSchedule.id)) {
            is Resource.Success -> {
                val schedule = scheduleDB.data ?: return false
                return schedule.settings.schedule.isAutoUpdate
            }
            is Resource.Error -> {
                return false
            }
        }
    }

    // FIXME Rewrite
    private suspend fun updateSchedules(schedules: ArrayList<SavedSchedule>): ArrayList<SavedSchedule> {
        for (savedSchedule in schedules) {
            val isScheduleAutoUpdate = getScheduleAutoUpdateStatus(savedSchedule)
            if (!isScheduleAutoUpdate || savedSchedule.lastUpdateDate == null) continue
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
                savedScheduleList.map { savedSchedule ->
                    when (
                        val lastUpdatedDate = if (savedSchedule.isGroup) {
                            getScheduleLastUpdateUseCase.getGroupLastUpdateDateByID(savedSchedule.id)
                        } else {
                            getScheduleLastUpdateUseCase.getEmployeeLastUpdateDateByID(savedSchedule.id)
                        }
                    ) {
                        is Resource.Success -> {
                            val lastUpdate = lastUpdatedDate.data ?: ScheduleLastUpdatedDate.empty
                            if (savedSchedule.lastUpdateDate != null
                                && (lastUpdate.lastUpdateDate ?: "") != savedSchedule.lastUpdateDate) {
                                savedSchedule.lastUpdateDate = lastUpdate.lastUpdateDate ?: ""
                                shouldUpdateSavedSchedule.add(savedSchedule)
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
                return Resource.Error(
                    errorType = StatusCode.SERVER_ERROR
                )
            }
        }

        return Resource.Success(
            data = shouldUpdateSavedSchedule
        )

    }

}


