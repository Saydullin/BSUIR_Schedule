package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.*
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.domain.repository.EmployeeItemsRepository
import com.bsuir.bsuirschedule.domain.repository.GroupItemsRepository
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.ScheduleController

class EmployeeScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val employeeItemsRepository: EmployeeItemsRepository,
    private val groupItemsRepository: GroupItemsRepository,
    private val fullScheduleUseCase: FullScheduleUseCase,
    private val currentWeekUseCase: GetCurrentWeekUseCase
) {

    suspend fun getScheduleAPI(urlId: String): Resource<Schedule> {

        return try {
            when (
                val apiSchedule = scheduleRepository.getEmployeeScheduleAPI(urlId)
            ) {
                is Resource.Success -> {
                    val data = apiSchedule.data!!
                    when (
                        val groupItems = groupItemsRepository.getAllGroupItems()
                    ) {
                        is Resource.Success -> {
                            // Add group models to employee's student groups
                            val currentWeek = currentWeekUseCase.getCurrentWeek()
                            if (currentWeek is Resource.Error) {
                                return Resource.Error(
                                    errorType = currentWeek.errorType,
                                    message = currentWeek.message
                                )
                            }
                            val schedule = getNormalSchedule(data, currentWeek.data!!)
                            setActualSettings(schedule)
                            fullScheduleUseCase.mergeGroupsSubjects(schedule, groupItems.data!!)
                            val isMergedDepartments = mergeDepartments(schedule)
                            if (isMergedDepartments is Resource.Error) {
                                return Resource.Error(
                                    errorType = isMergedDepartments.errorType,
                                    message = isMergedDepartments.message
                                )
                            }
                            schedule.id = schedule.employee.id
                            return Resource.Success(schedule)
                        }
                        is Resource.Error -> {
                            return Resource.Error(
                                errorType = groupItems.errorType,
                                message = groupItems.message
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    return Resource.Error(
                        errorType = apiSchedule.errorType,
                        message = apiSchedule.message
                    )
                }
            }
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.DATA_ERROR,
                message = e.message
            )
        }
    }

    private suspend fun setActualSettings(schedule: Schedule) {
        val foundSchedule = getScheduleById(schedule.id)
        if (foundSchedule is Resource.Success) {
            schedule.settings = foundSchedule.data!!.settings
        }
    }

    private fun getNormalSchedule(groupSchedule: GroupSchedule, currentWeekNumber: Int): Schedule {
        val scheduleController = ScheduleController()

        return scheduleController.getBasicSchedule(groupSchedule, currentWeekNumber)
    }

    private suspend fun mergeDepartments(schedule: Schedule): Resource<Schedule> {
        return when (
            val result = employeeItemsRepository.getEmployeeItems()
        ) {
            is Resource.Success -> {
                val data = result.data!!
                if (schedule.employee.id == -1) {
                    return Resource.Error(
                        errorType = Resource.DATA_ERROR,
                        message = "Employee field is null, cannot add departments list"
                    )
                }
                val employeeMatch = data.find { it.id == schedule.employee.id }
                schedule.employee.departmentsList = employeeMatch?.departments
                Resource.Success(null)
            }
            is Resource.Error -> {
                Resource.Error(
                    errorType = result.errorType,
                    message = result.message
                )
            }
        }
    }

    suspend fun getScheduleById(employeeId: Int): Resource<Schedule> {
        return try {
            when (
                val result = scheduleRepository.getScheduleById(employeeId)
            ) {
                is Resource.Success -> {
                    val data = result.data!!
                    Resource.Success(data)
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

    suspend fun getFullSchedule(groupSchedule: GroupSchedule): Resource<Schedule> {
        val currentWeek = currentWeekUseCase.getCurrentWeek()
        if (currentWeek is Resource.Error) {
            return Resource.Error(
                errorType = currentWeek.errorType
            )
        }
        return fullScheduleUseCase.getSchedule(groupSchedule, currentWeek.data!!)
    }

    suspend fun saveSchedule(schedule: Schedule): Resource<Unit> {
        return scheduleRepository.saveSchedule(schedule)
    }

    suspend fun updateScheduleSettings(id: Int, newSchedule: ScheduleSettings): Resource<Unit> {
        return scheduleRepository.updateScheduleSettings(id, newSchedule)
    }

    suspend fun deleteSchedule(savedSchedule: SavedSchedule): Resource<Unit> {
        return scheduleRepository.deleteEmployeeSchedule(savedSchedule.employee.urlId)
    }

}