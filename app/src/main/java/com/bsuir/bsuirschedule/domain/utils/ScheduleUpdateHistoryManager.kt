package com.bsuir.bsuirschedule.domain.utils

import com.bsuir.bsuirschedule.domain.models.*

class ScheduleUpdateHistoryManager (
    private val previousSchedule: Schedule,
    private val currentSchedule: Schedule
) {

    fun getChangedDays(): ArrayList<ScheduleDayUpdateHistory> {
        val scheduleChangedDays = ArrayList<ScheduleDayUpdateHistory>()
        val previousSchedules = previousSchedule.schedules
        val currentSchedules = currentSchedule.schedules

        if (previousSchedules == currentSchedules) return scheduleChangedDays
        val changedDays = currentSchedules.minus(previousSchedules)

        changedDays.map { scheduleDay ->
            val dateInMillis = scheduleDay.dateInMillis
            val previousScheduleDay = previousSchedules.find { it.dateInMillis == dateInMillis }
            val previousDaySubjects = previousScheduleDay?.schedule ?: ArrayList()
            val subjectsHistory = ArrayList<ScheduleSubjectHistory>()

            /** null = day was added in new schedule */
            if (previousScheduleDay?.schedule == null) {
                scheduleChangedDays.add(scheduleDay.toScheduleDayUpdatedHistory(SubjectHistoryStatus.ADDED))
                return@map
            }

            /** true = schedule days are the same */
            if (previousDaySubjects == scheduleDay.schedule) return@map

            val deletedSubjects = previousDaySubjects.minus(scheduleDay.schedule)
            val addedSubjects = scheduleDay.schedule.minus(previousDaySubjects)

            for (subject in scheduleDay.schedule) {
                if (addedSubjects.contains(subject)) {
                    subjectsHistory.add(subject.toSubjectHistory(status = SubjectHistoryStatus.ADDED))
                    continue
                }
                if (deletedSubjects.contains(subject)) {
                    subjectsHistory.add(subject.toSubjectHistory(status = SubjectHistoryStatus.DELETED))
                    continue
                }
                subjectsHistory.add(subject.toSubjectHistory())
            }

            scheduleChangedDays.add(scheduleDay.toScheduleDayUpdatedHistory(SubjectHistoryStatus.ADDED, scheduleSubjects = subjectsHistory))
        }

        return scheduleChangedDays
    }

}


