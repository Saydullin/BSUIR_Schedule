package com.bsuir.bsuirschedule.domain.usecase.schedule

import android.util.Log
import com.bsuir.bsuirschedule.domain.models.*
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
                    if (currentWeek is Resource.Error) {
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
                    val schedule = getNormalSchedule(
                        data,
                        holidays.data!!,
                        currentWeek.data!!
                    )
                    Log.e("sady", "exams check2 ${data.examsSchedule.toString()}")
                    setActualSettings(schedule)
                    val isMergedFacultyAndSpeciality = mergeSpecialitiesAndFaculties(schedule)
                    if (isMergedFacultyAndSpeciality is Resource.Error) {
                        return isMergedFacultyAndSpeciality
                    }
                    val isMergedEmployees = mergeEmployeeItems(schedule)
                    if (isMergedEmployees is Resource.Error) {
                        return isMergedEmployees
                    }
                    Log.e("sady", "exams ${isMergedEmployees.data?.examsSchedule?.size}")
                    Log.e("sady", "exams ${isMergedEmployees.data?.exams?.size}")
                    //setPrevOriginalSchedule(schedule)
                    if (schedule.schedules.isNotEmpty()) {
                        schedule.settings.term.selectedTerm = ScheduleTerm.CURRENT_SCHEDULE
                    } else if (schedule.previousSchedules.isNotEmpty()) {
                        schedule.settings.term.selectedTerm = ScheduleTerm.PREVIOUS_SCHEDULE
                    } else if (schedule.previousSchedules.isEmpty()
                        && !schedule.isExamsNotExist()) {
                        schedule.settings.term.selectedTerm = ScheduleTerm.SESSION
                    }
                    Resource.Success(schedule)
                }
                is Resource.Error -> {
                    Log.e("sady", "getGroupAPI UseCase updated")
                    Resource.Error(
                        statusCode = apiSchedule.statusCode,
                        message = apiSchedule.message
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("sady", "getGroupAPI UseCase updated 2")
            Log.e("sady", e.printStackTrace().toString())
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
                    when (
                        val groupItems = groupItemsRepository.getAllGroupItems()
                    ) {
                        is Resource.Success -> {
                            val currentWeek = currentWeekUseCase.getCurrentWeek()
                            if (currentWeek is Resource.Error) {
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
                            val schedule = getNormalSchedule(
                                data,
                                holidays.data!!,
                                currentWeek.data!!
                            )
                            setActualSettings(schedule)
                            mergeGroupsSubjects(schedule, groupItems.data!!)
                            val isMergedDepartments = mergeDepartments(schedule)
                            if (isMergedDepartments is Resource.Error) {
                                return isMergedDepartments
                            }
                            //setPrevOriginalSchedule(schedule)
                            return Resource.Success(schedule)
                        }
                        is Resource.Error -> {
                            return Resource.Error(
                                statusCode = groupItems.statusCode,
                                message = groupItems.message
                            )
                        }
                    }
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

    private fun mergeGroupsSubjects(schedule: Schedule, groupItems: ArrayList<Group>) {
        val scheduleController = ScheduleController()
        scheduleController.mergeGroupsSubjects(schedule.schedules, groupItems)
        scheduleController.mergeGroupsSubjects(schedule.examsSchedule, groupItems)
    }

    private suspend fun mergeDepartments(schedule: Schedule): Resource<Schedule> {
        return when (
            val result = employeeItemsRepository.getEmployeeItems()
        ) {
            is Resource.Success -> {
                val data = result.data!!
                if (schedule.employee.id == -1) {
                    return Resource.Error(
                        statusCode = StatusCode.DATA_ERROR
                    )
                }
                val employeeMatch = data.find { it.id == schedule.employee.id }
                schedule.employee.departmentsList = employeeMatch?.departments
                Resource.Success(null)
            }
            is Resource.Error -> {
                Resource.Error(
                    statusCode = result.statusCode,
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
                                employeeList.add(employee.toEmployeeSubject().copy(email = employeeItem.email))
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
                    statusCode = result.statusCode,
                    message = result.message
                )
            }
        }
    }

    private suspend fun mergeSpecialitiesAndFaculties(schedule: Schedule): Resource<Schedule> {
        val groupItems = groupItemsRepository.getAllGroupItems()

        return if (groupItems is Resource.Success && groupItems.data != null) {
            val data = groupItems.data
            if (schedule.group.id == -1) {
                return Resource.Error(
                    statusCode = StatusCode.DATA_ERROR
                )
            }
            val groupMatch = data.find { it.id == schedule.group.id }
            schedule.group.speciality = groupMatch?.speciality
            schedule.group.faculty = groupMatch?.faculty
            Resource.Success(schedule)
        } else {
            Resource.Error(
                statusCode = groupItems.statusCode,
                message = groupItems.message
            )
        }
    }

    private fun getNormalSchedule(
        groupSchedule: GroupSchedule,
        holidays: List<Holiday>,
        currentWeekNumber: Int,
        ): Schedule {
        val scheduleController = ScheduleController()
        val originalSchedule = scheduleController.getOriginalSchedule(groupSchedule)

        val normalSchedule = scheduleController.getBasicSchedule(
            groupSchedule,
            holidays,
            currentWeekNumber
        )
        normalSchedule.originalSchedule = originalSchedule

        Log.e("sady", "getNormalSchedule ${normalSchedule.examsSchedule.size}")

        return normalSchedule
    }

    private suspend fun setPrevOriginalSchedule(schedule: Schedule) {
        val oldSchedule = scheduleRepository.getScheduleById(schedule.id)

        if (oldSchedule is Resource.Success && oldSchedule.data != null) {
            if (oldSchedule.data.originalSchedule != schedule.originalSchedule) {
                schedule.prevOriginalSchedule = oldSchedule.data.originalSchedule
                schedule.lastUpdateTime = Date().time
            }
        }
    }

    private suspend fun setActualSettings(schedule: Schedule) {
        val foundSchedule = getById(schedule.id)
        if (foundSchedule is Resource.Success) {
            schedule.settings = foundSchedule.data!!.settings
        }
    }

    suspend fun getById(groupId: Int, ignoreSettings: Boolean = false): Resource<Schedule> {
        return try {
            when (
                val result = scheduleRepository.getScheduleById(groupId)
            ) {
                is Resource.Success -> {
                    val data = result.data!!
                    val scheduleController = ScheduleController()
                    val schedule = scheduleController.getRegularSchedule(data, ignoreSettings)

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


