package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.*
import com.bsuir.bsuirschedule.domain.repository.EmployeeItemsRepository
import com.bsuir.bsuirschedule.domain.repository.GroupItemsRepository
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class GroupScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val groupItemsRepository: GroupItemsRepository,
    private val employeeItemsRepository: EmployeeItemsRepository,
    private val fullScheduleUseCase: FullScheduleUseCase,
    private val examsScheduleUseCase: FullExamsScheduleUseCase,
    private val currentWeekUseCase: GetCurrentWeekUseCase
) {

    suspend fun getGroupScheduleAPI(groupName: String): Resource<GroupSchedule> {

        return try {
            when (
                val apiSchedule = scheduleRepository.getScheduleAPI(groupName)
            ) {
                is Resource.Success -> {
                    val data = apiSchedule.data!!
                    data.id = data.group?.id ?: -1
                    val isMergedFacultyAndSpeciality = mergeSpecialitiesAndFaculties(data)
                    if (isMergedFacultyAndSpeciality is Resource.Error) {
                        return Resource.Error(
                            errorType = isMergedFacultyAndSpeciality.errorType,
                            message = isMergedFacultyAndSpeciality.message
                        )
                    }
                    val isMergedEmployees = mergeEmployeeItems(data)
                    if (isMergedEmployees is Resource.Error) {
                        return Resource.Error(
                            errorType = isMergedEmployees.errorType,
                            message = isMergedEmployees.message
                        )
                    }
                    Resource.Success(data)
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

    private suspend fun mergeEmployeeItems(groupSchedule: GroupSchedule): Resource<GroupSchedule> {
        return when (
            val result = employeeItemsRepository.getEmployeeItems()
        ) {
            is Resource.Success -> {
                val data = result.data!!
                groupSchedule.schedules?.getList()?.map { day ->
                    day.map { subject ->
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
                Resource.Success(groupSchedule)
            }
            is Resource.Error -> {
                Resource.Error(
                    errorType = result.errorType,
                    message = result.message
                )
            }
        }
    }

    private suspend fun mergeSpecialitiesAndFaculties(groupSchedule: GroupSchedule): Resource<GroupSchedule> {
        return when (
            val result = groupItemsRepository.getAllGroupItems()
        ) {
            is Resource.Success -> {
                val data = result.data!!
                if (groupSchedule.group == null) {
                    return Resource.Error(
                        errorType = Resource.DATA_ERROR,
                        message = "Group field is null, cannot add specialities and faculties list"
                    )
                }
                val groupMatch = data.find { it.id == groupSchedule.group.id }
                groupSchedule.group.speciality = groupMatch?.speciality
                groupSchedule.group.faculty = groupMatch?.faculty
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

    suspend fun getFullSchedule(groupSchedule: GroupSchedule): Resource<Schedule> {
        val currentWeek = currentWeekUseCase.getCurrentWeek()
        if (currentWeek is Resource.Error) {
            return Resource.Error(
                errorType = currentWeek.errorType
            )
        }
        return fullScheduleUseCase.getSchedule(groupSchedule, currentWeek.data!!.week)
    }

    suspend fun getScheduleById(groupId: Int): Resource<GroupSchedule> {
        return try {
            when (
                val result = scheduleRepository.getScheduleById(groupId)
            ) {
                is Resource.Success -> {
                    val data = result.data!!
                    if (!data.isNotExistExams()) {
                        val examsSchedule = examsScheduleUseCase.getSchedule(data.exams!!, data.startExamsDate!!, data.endExamsDate!!)
                        data.examsSchedule = examsSchedule
                    }
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
            e.printStackTrace()
            Resource.Error(
                errorType = Resource.DATA_ERROR,
                message = e.message
            )
        }
    }

    suspend fun saveSchedule(schedule: GroupSchedule): Resource<Unit> {
        return scheduleRepository.saveSchedule(schedule)
    }

    suspend fun deleteSchedule(savedSchedule: SavedSchedule): Resource<Unit> {
        return scheduleRepository.deleteGroupSchedule(savedSchedule.group.name)
    }

}


