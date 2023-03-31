package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.ScheduleController
import com.bsuir.bsuirschedule.domain.utils.StatusCode

class AddScheduleSubjectUseCase(
    private val getScheduleUseCase: GetScheduleUseCase,
    private val saveScheduleUseCase: SaveScheduleUseCase,
) {

    private fun addCustomSubject(schedule: Schedule, subject: ScheduleSubject): Schedule {
        val scheduleController = ScheduleController()

        return scheduleController.addCustomSubject(schedule, subject)
    }

    suspend fun execute(scheduleId: Int, subject: ScheduleSubject): Resource<Unit> {
        val schedule = getScheduleUseCase.getById(scheduleId, ignoreSettings = true)
        return if (schedule is Resource.Success) {
            if (schedule.data != null) {
                val newSchedule = addCustomSubject(schedule.data, subject)
                return saveScheduleUseCase.execute(newSchedule)
            } else {
                Resource.Error(StatusCode.DATA_ERROR)
            }
        } else {
            return Resource.Error(schedule.statusCode)
        }
    }

}


