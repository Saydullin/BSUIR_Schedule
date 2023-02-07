package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.WidgetSchedule
import com.bsuir.bsuirschedule.domain.usecase.SharedPrefsUseCase
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.domain.utils.WidgetSubjectController

class GetActualScheduleDayUseCase(
    private val sharedPrefsUseCase: SharedPrefsUseCase,
    private val getScheduleUseCase: GetScheduleUseCase
) {

    suspend fun execute(): WidgetSchedule {
        val activeScheduleId = sharedPrefsUseCase.getActiveScheduleId()
        val activeScheduleResource = getScheduleUseCase.getById(activeScheduleId, 0, -1)

        if (activeScheduleResource is Resource.Success) {
            val activeSchedule = activeScheduleResource.data
            val actualDay = getActualDay(activeSchedule)

            return WidgetSchedule(
                actualDay != null,
                schedule = activeSchedule,
                activeScheduleDay = actualDay
            )
        }

        return WidgetSchedule.notExist
    }

    private fun getActualDay(schedule: Schedule?): ScheduleDay? {
        if (schedule == null) return null
        val widgetSubjectController = WidgetSubjectController(schedule)

        return widgetSubjectController.getActualScheduleDay()
    }

}


