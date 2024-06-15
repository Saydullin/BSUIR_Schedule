package com.bsuir.bsuirschedule.domain.manager.schedule.component

import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.SubjectBreakTime
import com.bsuir.bsuirschedule.domain.utils.CalendarDate

class ScheduleBreakTime(
    private val scheduleDays: ArrayList<ScheduleDay>,
) {

    fun execute(): ArrayList<ScheduleDay> {
        val subjectsBreakTime = ArrayList<ScheduleDay>()
        val calendarDate = CalendarDate()

        for (scheduleItem in scheduleDays) {
            val scheduleDay = scheduleItem.copy()
            val subjectsList = ArrayList<ScheduleSubject>()
            for (subjectIndex in 0 until scheduleItem.schedule.size) {
                val subject = scheduleItem.schedule[subjectIndex].copy()
                if (subjectIndex != 0) {
                    val currSubject = scheduleItem.schedule[subjectIndex]
                    var prevSubject = scheduleItem.schedule[subjectIndex - 1]
                    for (subjectIdx in subjectIndex - 1 downTo 0) {
                        if (scheduleItem.schedule[subjectIdx].isIgnored != true) {
                            prevSubject = scheduleItem.schedule[subjectIdx]
                            break
                        }
                    }
                    subject.breakTime = calendarDate.getSubjectBreakTime(
                        currSubject.startLessonTime,
                        prevSubject.endLessonTime
                    )
                } else {
                    subject.breakTime = SubjectBreakTime(
                        -1,
                        -1,
                        false
                    )
                }
                subjectsList.add(subject)
            }
            scheduleDay.schedule = subjectsList
            subjectsBreakTime.add(scheduleDay)
        }

        return subjectsBreakTime
    }

}