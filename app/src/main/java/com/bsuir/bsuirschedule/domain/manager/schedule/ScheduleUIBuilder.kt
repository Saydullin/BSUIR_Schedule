package com.bsuir.bsuirschedule.domain.manager.schedule

import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings

class ScheduleUIBuilder {

    private var scheduleFromDB: Schedule? = null

    fun setSchedule(schedule: Schedule): ScheduleUIBuilder {
        scheduleFromDB = schedule
        return this
    }

    private fun initSchedule(
        scheduleManager: ScheduleManager,
        scheduleDays: ArrayList<ScheduleDay>,
        scheduleSettings: ScheduleSettings,
        startDate: String,
        filterPastDays: Boolean = true
    ): ArrayList<ScheduleDay> {

        val filteredScheduleByActualDates = if (filterPastDays) {
            scheduleManager.filterActualScheduleBySettings(
                scheduleSettings = scheduleSettings.schedule,
                scheduleDays = scheduleDays,
                startDate = startDate
            )
        } else {
            scheduleDays
        }
        val filteredScheduleBySubgroup = scheduleManager.filterScheduleSubgroupBySettings(
            scheduleSettings = scheduleSettings,
            scheduleDays = filteredScheduleByActualDates
        )
        val scheduleBreakTime = scheduleManager.setSubjectsBreakTime(
            scheduleDays = filteredScheduleBySubgroup
        )

        return scheduleBreakTime
    }

    fun build(): Schedule {
        val schedule = scheduleFromDB ?:
            throw IllegalStateException("schedule is null in ScheduleUIBuilder")

        val scheduleManager = ScheduleManager()

        val initSchedule = initSchedule(
            scheduleManager = scheduleManager,
            scheduleDays = schedule.schedules,
            scheduleSettings = schedule.settings,
            startDate = schedule.startDate
        )
        val initPreviousSchedule = initSchedule(
            scheduleManager = scheduleManager,
            scheduleDays = schedule.previousSchedules,
            scheduleSettings = schedule.settings,
            startDate = schedule.startDate,
            filterPastDays = false
        )

        schedule.schedules = initSchedule
        schedule.previousSchedules = initPreviousSchedule

        return schedule
    }

}


