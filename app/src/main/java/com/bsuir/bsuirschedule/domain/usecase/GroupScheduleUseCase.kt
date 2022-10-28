package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.*
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.domain.repository.CurrentWeekRepository
import com.bsuir.bsuirschedule.domain.repository.EmployeeItemsRepository
import com.bsuir.bsuirschedule.domain.repository.GroupItemsRepository
import com.bsuir.bsuirschedule.domain.repository.ScheduleRepository
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.ScheduleController

class GroupScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
    groupItemsRepository: GroupItemsRepository,
    employeeItemsRepository: EmployeeItemsRepository,
    currentWeekUseCase: GetCurrentWeekUseCase
): ScheduleUseCase(
    scheduleRepository = scheduleRepository,
    groupItemsRepository = groupItemsRepository,
    employeeItemsRepository = employeeItemsRepository,
    currentWeekUseCase = currentWeekUseCase
) {

    suspend fun updateScheduleSettings(id: Int, newSchedule: ScheduleSettings): Resource<Unit> {
        return scheduleRepository.updateScheduleSettings(id, newSchedule)
    }

    suspend fun saveSchedule(schedule: Schedule): Resource<Unit> {
        return scheduleRepository.saveSchedule(schedule)
    }

    suspend fun deleteSchedule(savedSchedule: SavedSchedule): Resource<Unit> {
        return scheduleRepository.deleteGroupSchedule(savedSchedule.group.name)
    }

}


