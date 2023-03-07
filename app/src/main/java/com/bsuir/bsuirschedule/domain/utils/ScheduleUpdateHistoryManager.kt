package com.bsuir.bsuirschedule.domain.utils

import android.util.Log
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
        Log.e("sady", "currentScheduleSize ${currentSchedules.size}, previousScheduleSize ${previousSchedules.size}")
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

            /** true = schedule days are the same */
//            if (previousDaySubjects == scheduleDay.schedule) return@map

            val deletedSubjects = previousDaySubjects.minus(scheduleDay.schedule.toHashSet()) // error
            Log.e("sady", "deletedSubjects ${deletedSubjects.size}, $deletedSubjects")
            val addedSubjects = scheduleDay.schedule.minus(previousDaySubjects.toHashSet())
            Log.e("sady", "addedSubjects ${addedSubjects.size}, $addedSubjects")

            deletedSubjects.map {
                insertSubject(scheduleDay.schedule, it)
            }

            /** scheduleDay.schedule + deletedSubjects */

            for (subject in scheduleDay.schedule) {
                Log.e("sady", "id: ${subject.startLessonTime} ${subject.subject} ${subject.lessonTypeAbbrev}")
                if (addedSubjects.contains(subject)) {
                    Log.e("sady", "added subject added")
                    subjectsHistory.add(subject.toSubjectHistory(status = SubjectHistoryStatus.ADDED))
                    continue
                }
                if (deletedSubjects.contains(subject)) { // ?
                    Log.e("sady", "deleted subject added")
                    subjectsHistory.add(subject.toSubjectHistory(status = SubjectHistoryStatus.DELETED))
                    continue
                }
                subjectsHistory.add(subject.toSubjectHistory())
            }

            scheduleChangedDays.add(scheduleDay.toScheduleDayUpdatedHistory(SubjectHistoryStatus.ADDED, scheduleSubjects = subjectsHistory))
        }

        Log.e("sady", "scheduleChangedDays ${scheduleChangedDays.size}, $scheduleChangedDays")
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


