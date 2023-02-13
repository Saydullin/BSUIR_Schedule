package com.bsuir.bsuirschedule.domain.utils

import android.util.Log
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleUpdate

class ScheduleUpdateHistoryManager (
    private val scheduleUpdate: ScheduleUpdate
) {

    fun getChangedDays(): ArrayList<ScheduleDay> {
        val scheduleChangedDays = ArrayList<ScheduleDay>()
        val previousSchedules = scheduleUpdate.previousSchedule.schedules
        val currentSchedules = scheduleUpdate.currentSchedule.schedules

        Log.e("sady", "17")
        if (previousSchedules == currentSchedules) return scheduleChangedDays
        Log.e("sady", "19")
        val changedDays = currentSchedules.minus(previousSchedules.toHashSet())
        Log.e("sady", "21 (${changedDays.size})")
        changedDays.map { scheduleDay ->
            val dateInMillis = scheduleDay.dateInMillis
            // ?
            val previousScheduleDay = previousSchedules.find { it.dateInMillis == dateInMillis }

            val previousDaySubjects = ArrayList<ScheduleSubject>()

            /** null = day was added in new schedule */
            if (previousScheduleDay?.schedule == null) {
                scheduleChangedDays.add(scheduleDay)
                Log.e("sady", "32")
                return@map
            }
            /** true = schedules are the same */
            Log.e("sady", "36")
            if (previousDaySubjects == scheduleDay.schedule) return@map

            val addedSubjects = scheduleDay.schedule.minus(previousDaySubjects.toHashSet())

            scheduleDay.schedule.map { subject ->
                if (addedSubjects.contains(subject)) {
                    subject.lessonTypeAbbrev = ScheduleSubject.LESSON_TYPE_ADDED
                } else {
                    subject.lessonTypeAbbrev = ScheduleSubject.LESSON_TYPE_DELETED
                }
            }

            scheduleChangedDays.add(scheduleDay)
        }

        Log.e("sady", "44")
        return scheduleChangedDays
    }

}