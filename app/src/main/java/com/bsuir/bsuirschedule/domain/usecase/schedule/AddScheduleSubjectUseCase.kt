package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.manager.schedule.component.ScheduleBreakTime
import com.bsuir.bsuirschedule.domain.models.*
import com.bsuir.bsuirschedule.domain.usecase.GetCurrentWeekUseCase
import com.bsuir.bsuirschedule.domain.usecase.GetEmployeeItemsUseCase
import com.bsuir.bsuirschedule.domain.usecase.GetGroupItemsUseCase
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.ScheduleController
import com.bsuir.bsuirschedule.domain.utils.StatusCode

class AddScheduleSubjectUseCase(
    private val getScheduleUseCase: GetScheduleUseCase,
    private val saveScheduleUseCase: SaveScheduleUseCase,
    private val getCurrentWeekUseCase: GetCurrentWeekUseCase,
    private val getGroupItemsUseCase: GetGroupItemsUseCase,
    private val getEmployeeItemsUseCase: GetEmployeeItemsUseCase,
) {

    private suspend fun addCustomSubject(
        schedule: Schedule,
        currentWeekNumber: Int,
        subject: ScheduleSubject,
        sourceItemsText: String,
        scheduleTerm: ScheduleTerm,
    ): Schedule {
        val scheduleController = ScheduleController()

        setSubjectSourceItems(schedule.isGroup(), sourceItemsText, subject)

        val scheduleRes = scheduleController.addCustomSubject(
            schedule,
            currentWeekNumber,
            subject,
            scheduleTerm
        )

        return scheduleRes
    }

    private suspend fun setSubjectSourceItems(isGroup: Boolean, sourceItemsText: String, subject: ScheduleSubject) {
        val sourceItems = sourceItemsText
            .replace(" ", "")
            .replace(",", " ")
            .split(" ")

        if (sourceItems.isEmpty()) return
        if (isGroup) {
            val employeeItems = ArrayList<EmployeeSubject>()
            sourceItems.map { item ->
                val employeeItemResult = getEmployeeItemsUseCase.getEmployeeItems()
                if (employeeItemResult is Resource.Success && !employeeItemResult.data.isNullOrEmpty()) {
                    val employeeItem = employeeItemResult.data.firstOrNull {
                        it.getName().replace(" ", "") == item
                    }
                    if (employeeItem != null) {
                        employeeItems.add(employeeItem.toEmployeeSubject())
                    }
                }
            }
            subject.employees = employeeItems
        } else {
            val groupItems = ArrayList<Group>()
            val subjectGroupsItems = ArrayList<GroupSubject>()
            sourceItems.map { item ->
                val groupItemResult = getGroupItemsUseCase.getAllGroupItems()
                if (groupItemResult is Resource.Success && !groupItemResult.data.isNullOrEmpty()) {
                    val groupItem = groupItemResult.data.firstOrNull {
                        it.name.replace(" ", "") == item
                    }
                    if (groupItem != null) {
                        groupItems.add(groupItem)
                        subjectGroupsItems.add(GroupSubject(
                            specialityName = groupItem.speciality?.name ?: "",
                            specialityCode = groupItem.speciality?.code ?: "",
                            numberOfStudents = 0,
                            name = groupItem.name,
                            educationDegree = 0,
                        ))
                    }
                }
            }
            subject.groups = groupItems
            subject.subjectGroups = subjectGroupsItems
        }
    }

    suspend fun execute(
        scheduleId: Int,
        subject: ScheduleSubject,
        sourceItemsText: String,
        scheduleTerm: ScheduleTerm,
    ): Resource<Unit> {
        val schedule = getScheduleUseCase.getById(scheduleId)
        val currentWeekNumber = getCurrentWeekUseCase.getCurrentWeek()

        if (currentWeekNumber is Resource.Error || currentWeekNumber.data == null) {
            return Resource.Error(currentWeekNumber.statusCode)
        }

        return try {
            if (schedule is Resource.Success) {
                if (schedule.data != null) {
                    val newSchedule = addCustomSubject(
                        schedule.data,
                        currentWeekNumber.data,
                        subject,
                        sourceItemsText,
                        scheduleTerm,
                    )
                    // set break time, set sort

                    return saveScheduleUseCase.execute(newSchedule)
                } else {
                    Resource.Error(StatusCode.DATA_ERROR)
                }
            } else {
                return Resource.Error(schedule.statusCode)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return Resource.Error(StatusCode.DATA_ERROR)
        }
    }

}


