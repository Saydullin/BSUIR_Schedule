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
                    if (data.schedules != null) {
                        data.subgroups = getSubgroupsList(data.schedules.getList())
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

    private fun getSubgroupsList(schedule: List<ArrayList<ScheduleSubject>>): List<Int> {
        val amount = ArrayList<Int>()

        schedule.forEach { day ->
            day.forEach { subject ->
                amount.add(subject.numSubgroup ?: 0)
            }
        }

        return amount.toSet().toList()
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
        return fullScheduleUseCase.getSchedule(groupSchedule, currentWeek.data!!)
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

    suspend fun saveSchedule(schedule: GroupSchedule): Resource<Unit> {
        return scheduleRepository.saveSchedule(schedule)
    }

    suspend fun deleteSchedule(savedSchedule: SavedSchedule): Resource<Unit> {
        return scheduleRepository.deleteGroupSchedule(savedSchedule.group.name)
    }

}


