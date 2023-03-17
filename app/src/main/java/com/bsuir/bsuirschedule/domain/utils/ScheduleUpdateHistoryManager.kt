package com.bsuir.bsuirschedule.domain.utils

import com.bsuir.bsuirschedule.domain.models.*

class ScheduleUpdateHistoryManager (
    private val previousSchedule: Schedule,
    private val currentSchedule: Schedule
) {

    fun getChangedDays(): ArrayList<ScheduleDayUpdateHistory> {
        val scheduleChangedDays = ArrayList<ScheduleDayUpdateHistory>()
        val previousSchedules = previousSchedule.originalSchedule
        val currentSchedules = currentSchedule.originalSchedule

        if (previousSchedules == currentSchedules) return scheduleChangedDays
        val changedDays = currentSchedules.minus(previousSchedules.toHashSet())

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

            val deletedSubjects = previousDaySubjects.minus(scheduleDay.schedule.toHashSet()) // error
            val addedSubjects = scheduleDay.schedule.minus(previousDaySubjects.toHashSet())

            deletedSubjects.map {
                insertSubject(scheduleDay.schedule, it)
            }

            for (subject in scheduleDay.schedule) {
                if (addedSubjects.contains(subject)) {
                    subjectsHistory.add(subject.toSubjectHistory(status = SubjectHistoryStatus.ADDED))
                    continue
                }
                if (deletedSubjects.contains(subject)) { // ?
                    subjectsHistory.add(subject.toSubjectHistory(status = SubjectHistoryStatus.DELETED))
                    continue
                }
                subjectsHistory.add(subject.toSubjectHistory())
            }

            scheduleChangedDays.add(scheduleDay.toScheduleDayUpdatedHistory(SubjectHistoryStatus.ADDED, scheduleSubjects = subjectsHistory))
        }

        return scheduleChangedDays
    }

    private fun insertSubject(subjectsList: ArrayList<ScheduleSubject>, insertSubject: ScheduleSubject) {
        val subjectIdBeforeInsert = subjectsList.indexOfFirst { it.startMillis >= insertSubject.startMillis }

        if (subjectIdBeforeInsert == -1) {
            subjectsList.add(insertSubject)
        } else {
            subjectsList.add(subjectIdBeforeInsert, insertSubject)
        }
    }

}


