package com.bsuir.bsuirschedule.domain.usecase.schedule

import android.util.Log
import com.bsuir.bsuirschedule.domain.manager.schedule.ScheduleBuilder
import com.bsuir.bsuirschedule.domain.manager.schedule.ScheduleUIBuilder
import com.bsuir.bsuirschedule.domain.models.*
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.domain.repository.EmployeeItemsRepository
import com.bsuir.bsuirschedule.domain.repository.GroupItemsRepository
import com.bsuir.bsuirschedule.domain.repository.HolidayRepository
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.usecase.GetCurrentWeekUseCase
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.ScheduleController
import com.bsuir.bsuirschedule.domain.utils.StatusCode
import java.util.*
import kotlin.collections.ArrayList

class GetScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val groupItemsRepository: GroupItemsRepository,
    private val employeeItemsRepository: EmployeeItemsRepository,
    private val holidayRepository: HolidayRepository,
    private val currentWeekUseCase: GetCurrentWeekUseCase,
) {

    suspend fun getGroupAPI(groupName: String): Resource<Schedule> {

        return try {
            when (
                val apiSchedule = scheduleRepository.getGroupScheduleAPI(groupName)
            ) {
                is Resource.Success -> {
                    val data = apiSchedule.data!!
                    Log.e("sady", "exams check ${data.exams.toString()}")
                    val currentWeek = currentWeekUseCase.getCurrentWeek()
                    if (currentWeek is Resource.Error || currentWeek.data == null) {
                        return Resource.Error(
                            statusCode = currentWeek.statusCode,
                            message = currentWeek.message
                        )
                    }
                    val holidays = holidayRepository.getHolidays()
                    if (holidays is Resource.Error || holidays.data == null) {
                        return Resource.Error(
                            statusCode = holidays.statusCode,
                            message = holidays.message
                        )
                    }
                    val actualSettings = if (data.group?.id != null) {
                        getActualSettings(data.group.id)
                    } else {
                        null
                    }
                    val schedule = ScheduleBuilder()
                        .setGroupSchedule(data)
                        .setHolidays(holidays.data as ArrayList<Holiday>)
                        .setPreviousSchedule(true)
                        .setCurrentWeekNumber(currentWeek.data)
                        .setSettings(actualSettings)
                        .build()

                    return schedule
                }
                is Resource.Error -> {
                    Resource.Error(
                        statusCode = apiSchedule.statusCode,
                        message = apiSchedule.message
                    )
                }
            }
        } catch (e: Exception) {
            return Resource.Error(
                statusCode = StatusCode.DATA_ERROR,
                message = e.message
            )
        }
    }

    suspend fun getEmployeeAPI(urlId: String): Resource<Schedule> {

        return try {
            when (
                val apiSchedule = scheduleRepository.getEmployeeScheduleAPI(urlId)
            ) {
                is Resource.Success -> {
                    val data = apiSchedule.data!!
                    val currentWeek = currentWeekUseCase.getCurrentWeek()
                    if (currentWeek is Resource.Error || currentWeek.data == null) {
                        return Resource.Error(
                            statusCode = currentWeek.statusCode,
                            message = currentWeek.message
                        )
                    }
                    val holidays = holidayRepository.getHolidays()
                    if (holidays is Resource.Error) {
                        return Resource.Error(
                            statusCode = holidays.statusCode,
                            message = holidays.message
                        )
                    }
                    val groupItems = groupItemsRepository.getAllGroupItems()
                    val groups = groupItems.data ?: return Resource.Error(
                        statusCode = groupItems.statusCode,
                        message = groupItems.message
                    )
                    val actualSettings = if (data.group?.id != null) {
                        getActualSettings(data.group.id)
                    } else {
                        null
                    }
                    val schedule = ScheduleBuilder()
                        .setGroupSchedule(data)
                        .setHolidays(holidays.data as ArrayList<Holiday>)
                        .setPreviousSchedule(true)
                        .injectGroups(groups)
                        .setCurrentWeekNumber(currentWeek.data)
                        .setSettings(actualSettings)
                        .build()

                    return schedule
                }
                is Resource.Error -> {
                    return Resource.Error(
                        statusCode = apiSchedule.statusCode,
                        message = apiSchedule.message
                    )
                }
            }
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(
                statusCode = StatusCode.DATA_ERROR,
                message = e.message
            )
        }
    }

    private suspend fun getActualSettings(scheduleId: Int): ScheduleSettings? {
        val foundSchedule = getById(scheduleId)
        return if (foundSchedule is Resource.Success && foundSchedule.data != null) {
            foundSchedule.data.settings
        } else {
            null
        }
    }

    suspend fun getById(groupId: Int, ignoreSettings: Boolean = false): Resource<Schedule> {
        return try {
            when (
                val result = scheduleRepository.getScheduleById(groupId)
            ) {
                is Resource.Success -> {
                    val data = result.data ?:
                        return Resource.Error(
                            statusCode = StatusCode.DATABASE_ERROR
                        )
                    Log.e("sady", "schedule term: ${data.currentTerm}")
                    val schedule = ScheduleUIBuilder()
                        .setSchedule(data)
                        .build()

                    Resource.Success(schedule)
                }
                is Resource.Error -> {
                    Resource.Error(
                        statusCode = result.statusCode,
                        message = result.message
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                statusCode = StatusCode.DATA_ERROR,
                message = e.message
            )
        }
    }

}


