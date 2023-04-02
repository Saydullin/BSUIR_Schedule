package com.bsuir.bsuirschedule.domain.usecase.schedule

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
        sourceItemsText: String
    ): Schedule {
        val scheduleController = ScheduleController()

        setSubjectSourceItems(schedule.isGroup(), sourceItemsText, subject)

        return scheduleController.addCustomSubject(schedule, currentWeekNumber, subject)
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
                    val employeeItem = employeeItemResult.data.first {
                        it.getName().replace(" ", "") == item
                    }
                    employeeItems.add(employeeItem.toEmployeeSubject())
                }
            }
            subject.employees = employeeItems
        } else {
            val groupItems = ArrayList<Group>()
            val subjectGroupsItems = ArrayList<GroupSubject>()
            sourceItems.map { item ->
                val groupItemResult = getGroupItemsUseCase.getAllGroupItems()
                if (groupItemResult is Resource.Success && !groupItemResult.data.isNullOrEmpty()) {
                    val groupItem = groupItemResult.data.first {
                        it.name.replace(" ", "") == item
                    }
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
            subject.groups = groupItems
            subject.subjectGroups = subjectGroupsItems
        }
    }

    suspend fun execute(scheduleId: Int, subject: ScheduleSubject, sourceItemsText: String): Resource<Unit> {
        val schedule = getScheduleUseCase.getById(scheduleId, ignoreSettings = true)
        val currentWeekNumber = getCurrentWeekUseCase.getCurrentWeek()

        if (currentWeekNumber is Resource.Error || currentWeekNumber.data == null) {
            return Resource.Error(currentWeekNumber.statusCode)
        }

        return if (schedule is Resource.Success) {
            if (schedule.data != null) {
                val newSchedule = addCustomSubject(schedule.data, currentWeekNumber.data, subject, sourceItemsText)
                return saveScheduleUseCase.execute(newSchedule)
            } else {
                Resource.Error(StatusCode.DATA_ERROR)
            }
        } else {
            return Resource.Error(schedule.statusCode)
        }
    }

}

