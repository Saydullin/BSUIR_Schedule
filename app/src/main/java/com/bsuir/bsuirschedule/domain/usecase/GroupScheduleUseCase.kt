package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.*
import com.bsuir.bsuirschedule.domain.repository.EmployeeItemsRepository
import com.bsuir.bsuirschedule.domain.repository.GroupItemsRepository
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.ScheduleController

class GroupScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val groupItemsRepository: GroupItemsRepository,
    private val employeeItemsRepository: EmployeeItemsRepository,
    private val fullScheduleUseCase: FullScheduleUseCase,
    private val currentWeekUseCase: GetCurrentWeekUseCase
) {

    suspend fun getGroupScheduleAPI(groupName: String): Resource<Schedule> {

        return try {
            when (
                val apiSchedule = scheduleRepository.getScheduleAPI(groupName)
            ) {
                is Resource.Success -> {
                    val data = apiSchedule.data!!
                    data.id = data.group?.id ?: -1
                    val currentWeek = currentWeekUseCase.getCurrentWeek()
                    if (currentWeek is Resource.Error) {
                        return Resource.Error(
                            errorType = currentWeek.errorType,
                            message = currentWeek.message
                        )
                    }
                    val schedule = getNormalSchedule(data, currentWeek.data!!)
                    val isMergedFacultyAndSpeciality = mergeSpecialitiesAndFaculties(schedule)
                    if (isMergedFacultyAndSpeciality is Resource.Error) {
                        return Resource.Error(
                            errorType = isMergedFacultyAndSpeciality.errorType,
                            message = isMergedFacultyAndSpeciality.message
                        )
                    }
                    val isMergedEmployees = mergeEmployeeItems(schedule)
                    if (isMergedEmployees is Resource.Error) {
                        return Resource.Error(
                            errorType = isMergedEmployees.errorType,
                            message = isMergedEmployees.message
                        )
                    }
                    if (schedule.schedules.isNotEmpty()) {
                        schedule.subgroups = getSubgroupsList(schedule.schedules)
                    }
                    Resource.Success(schedule)
                }
                is Resource.Error -> {
                    Resource.Error(
                        errorType = apiSchedule.errorType,
                        message = apiSchedule.message
                    )
                }
            }
        } catch (e: Exception) {
            return Resource.Error(
                errorType = Resource.DATA_ERROR,
                message = e.message
            )
        }
    }

    private fun getNormalSchedule(groupSchedule: GroupSchedule, currentWeekNumber: Int): Schedule {
        val scheduleController = ScheduleController()

        return scheduleController.getBasicSchedule(groupSchedule, currentWeekNumber)
    }

    private fun getSubgroupsList(schedule: ArrayList<ScheduleDay>): List<Int> {
        val amount = ArrayList<Int>()

        schedule.forEach { day ->
            day.schedule.forEach { subject ->
                amount.add(subject.numSubgroup ?: 0)
            }
        }

        return amount.toSet().toList()
    }

    private suspend fun mergeEmployeeItems(schedule: Schedule): Resource<Schedule> {
        return when (
            val result = employeeItemsRepository.getEmployeeItems()
        ) {
            is Resource.Success -> {
                val data = result.data!!
                schedule.schedules.map { day ->
                    day.schedule.map { subject ->
                        val employeeList = ArrayList<EmployeeSubject>()
                        subject.employees?.forEach { employeeItem ->
                            val employee = data.find { it.id == employeeItem.id }
                            if (employee != null) {
                                employeeList.add(employee.toEmployeeSubject())
                            } else {
                                employeeList.add(employeeItem)
                            }
                        }
                        subject.employees = employeeList
                    }
                }
                Resource.Success(schedule)
            }
            is Resource.Error -> {
                Resource.Error(
                    errorType = result.errorType,
                    message = result.message
                )
            }
        }
    }

    private suspend fun mergeSpecialitiesAndFaculties(schedule: Schedule): Resource<Schedule> {
        return when (
            val result = groupItemsRepository.getAllGroupItems()
        ) {
            is Resource.Success -> {
                val data = result.data!!
                if (schedule.group.id == -1) {
                    return Resource.Error(
                        errorType = Resource.DATA_ERROR,
                        message = "Group field is null, cannot add specialities and faculties list"
                    )
                }
                val groupMatch = data.find { it.id == schedule.group.id }
                schedule.group.speciality = groupMatch?.speciality
                schedule.group.faculty = groupMatch?.faculty
                Resource.Success(schedule)
            }
            is Resource.Error -> {
                Resource.Error(
                    errorType = result.errorType,
                    message = result.message
                )
            }
        }
    }

    suspend fun getScheduleById(groupId: Int): Resource<Schedule> {
        return try {
            when (
                val result = scheduleRepository.getScheduleById(groupId)
            ) {
                is Resource.Success -> {
                    val data = result.data!!
                    val scheduleController = ScheduleController()
                    val currentWeek = currentWeekUseCase.getCurrentWeek()
                    if (currentWeek is Resource.Error) {
                        return Resource.Error(
                            errorType = currentWeek.errorType,
                            message = currentWeek.message
                        )
                    }
                    val currentWeekNumber = currentWeek.data!!
                    val schedule = scheduleController.getRegularSchedule(data, currentWeekNumber)
                    Resource.Success(schedule)
                }
                is Resource.Error -> {
                    Resource.Error(
                        errorType = result.errorType,
                        message = result.message
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.DATA_ERROR,
                message = e.message
            )
        }
    }

    suspend fun changeSubgroup(schedule: Schedule): Resource<Unit> {
        return when (
            val result = scheduleRepository.getScheduleById(schedule.id)
        ) {
            is Resource.Success -> {
                val groupSchedule = result.data
                    ?: return Resource.Error(
                        errorType = Resource.DATABASE_ERROR
                    )
                groupSchedule.selectedSubgroup = schedule.selectedSubgroup
                when (
                    val resultSave = scheduleRepository.saveSchedule(groupSchedule)
                ) {
                    is Resource.Success -> {
                        Resource.Success(null)
                    }
                    is Resource.Error -> {
                        Resource.Error(
                            errorType = resultSave.errorType,
                            message = resultSave.message
                        )
                    }
                }
            }
            is Resource.Error -> {
                return Resource.Error(
                    errorType = result.errorType,
                    message = result.message
                )
            }
        }
    }

    suspend fun saveSchedule(schedule: Schedule): Resource<Unit> {
        return scheduleRepository.saveSchedule(schedule)
    }

    suspend fun deleteSchedule(savedSchedule: SavedSchedule): Resource<Unit> {
        return scheduleRepository.deleteGroupSchedule(savedSchedule.group.name)
    }

}


