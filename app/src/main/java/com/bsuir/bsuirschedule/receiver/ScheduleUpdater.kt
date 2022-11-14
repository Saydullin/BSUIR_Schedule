package com.bsuir.bsuirschedule.receiver

import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.utils.ScheduleUpdateManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ScheduleUpdater : KoinComponent {

    private val scheduleUpdateManager: ScheduleUpdateManager by inject()

    fun execute(): ArrayList<SavedSchedule> {
        return scheduleUpdateManager.updatedSchedules()
    }

}


