package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.repository.EmployeeItemsRepository
import com.bsuir.bsuirschedule.domain.repository.GroupItemsRepository
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class EmployeeScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val employeeItemsRepository: EmployeeItemsRepository,
    private val groupItemsRepository: GroupItemsRepository,
    private val fullScheduleUseCase: FullScheduleUseCase,
    private val currentWeekUseCase: GetCurrentWeekUseCase
) {

    suspend fun getScheduleAPI(urlId: String): Resource<GroupSchedule> {

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
                            fullScheduleUseCase.mergeGroupsSubjects(data, groupItems.data!!)
                            val isMergedDepartments = mergeDepartments(data)
                            if (isMergedDepartments is Resource.Error) {
                                return Resource.Error(
                                    errorType = isMergedDepartments.errorType,
                                    message = isMergedDepartments.message
                                )
                            }
                        }
                        is Resource.Error -> {
                            return Resource.Error(
                                errorType = groupItems.errorType,
                                message = groupItems.message
                            )
                        }
                    }
                    data.id = data.employee?.id ?: -1
                    if (data.schedules != null) {
                        data.subgroups = getSubgroupsList(data.schedules.getList())
                    }
                    return Resource.Success(data)
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

    private fun getSubgroupsList(schedule: List<ArrayList<ScheduleSubject>>): List<Int> {
        val amount = ArrayList<Int>()

        schedule.forEach { day ->
            day.forEach { subject ->
                amount.add(subject.numSubgroup ?: 0)
            }
        }

        return amount.toSet().toList()
    }

    private suspend fun mergeDepartments(groupSchedule: GroupSchedule): Resource<GroupSchedule> {
        return when (
            val result = employeeItemsRepository.getEmployeeItems()
        ) {
            is Resource.Success -> {
                val data = result.data!!
                if (groupSchedule.employee == null) {
                    return Resource.Error(
                        errorType = Resource.DATA_ERROR,
                        message = "Employee field is null, cannot add departments list"
                    )
                }
                val employeeMatch = data.find { it.id == groupSchedule.employee.id }
                groupSchedule.employee.departmentsList = employeeMatch?.departments
                Resource.Success(GroupSchedule.empty)
            }
            is Resource.Error -> {
                Resource.Error(
                    errorType = result.errorType,
                    message = result.message
                )
            }
        }
    }

    suspend fun getScheduleById(employeeId: Int): Resource<GroupSchedule> {
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

    suspend fun saveSchedule(schedule: GroupSchedule): Resource<Unit> {
        return scheduleRepository.saveSchedule(schedule)
    }

    suspend fun deleteSchedule(savedSchedule: SavedSchedule): Resource<Unit> {
        return scheduleRepository.deleteEmployeeSchedule(savedSchedule.employee.urlId)
    }

}