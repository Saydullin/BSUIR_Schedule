package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.models.*
import com.bsuir.bsuirschedule.domain.repository.EmployeeItemsRepository
import com.bsuir.bsuirschedule.domain.repository.GroupItemsRepository
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.usecase.GetCurrentWeekUseCase
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.ScheduleController

class GetScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val groupItemsRepository: GroupItemsRepository,
    private val employeeItemsRepository: EmployeeItemsRepository,
    private val currentWeekUseCase: GetCurrentWeekUseCase
) {

    suspend fun getGroupAPI(groupName: String): Resource<Schedule> {

        return try {
            when (
                val apiSchedule = scheduleRepository.getGroupScheduleAPI(groupName)
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
                    setActualSettings(schedule)
                    val isMergedFacultyAndSpeciality = mergeSpecialitiesAndFaculties(schedule)
                    if (isMergedFacultyAndSpeciality is Resource.Error) {
                        return isMergedFacultyAndSpeciality
                    }
                    val isMergedEmployees = mergeEmployeeItems(schedule)
                    if (isMergedEmployees is Resource.Error) {
                        return isMergedEmployees
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

    suspend fun getEmployeeAPI(urlId: String): Resource<Schedule> {

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
                            val currentWeek = currentWeekUseCase.getCurrentWeek()
                            if (currentWeek is Resource.Error) {
                                return Resource.Error(
                                    errorType = currentWeek.errorType,
                                    message = currentWeek.message
                                )
                            }
                            val schedule = getNormalSchedule(data, currentWeek.data!!)
                            setActualSettings(schedule)
                            mergeGroupsSubjects(schedule, groupItems.data!!)
                            val isMergedDepartments = mergeDepartments(schedule)
                            if (isMergedDepartments is Resource.Error) {
                                return isMergedDepartments
                            }
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

    private fun mergeGroupsSubjects(schedule: Schedule, groupItems: ArrayList<Group>) {
        val scheduleController = ScheduleController()
        scheduleController.mergeGroupsSubjects(schedule, groupItems)
    }

    private suspend fun mergeDepartments(schedule: Schedule): Resource<Schedule> {
        return when (
            val result = employeeItemsRepository.getEmployeeItems()
        ) {
            is Resource.Success -> {
                val data = result.data!!
                if (schedule.employee.id == -1) {
                    return Resource.Error(
                        errorType = Resource.DATA_ERROR
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
                        errorType = Resource.DATA_ERROR
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

    private fun getNormalSchedule(groupSchedule: GroupSchedule, currentWeekNumber: Int): Schedule {
        val scheduleController = ScheduleController()

        return scheduleController.getBasicSchedule(groupSchedule, currentWeekNumber)
    }

    private suspend fun setActualSettings(schedule: Schedule) {
        val foundSchedule = getById(schedule.id, 1, 1)
        if (foundSchedule is Resource.Success) {
            schedule.settings = foundSchedule.data!!.settings
        }
    }

    suspend fun getById(groupId: Int, page: Int, pageSize: Int): Resource<Schedule> {
        return try {
            when (
                val result = scheduleRepository.getScheduleById(groupId)
            ) {
                is Resource.Success -> {
                    val data = result.data!!
                    val scheduleController = ScheduleController()
                    val schedule = scheduleController.getRegularSchedule(data, page, pageSize)
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

}


