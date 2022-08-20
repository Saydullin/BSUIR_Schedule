package com.example.bsuirschedule.domain.usecase

import com.example.bsuirschedule.domain.models.ScheduleDay
import com.example.bsuirschedule.domain.models.ScheduleSubject
import com.example.bsuirschedule.domain.utils.CalendarDate

class FullExamsScheduleUseCase {

    fun getSchedule(subjects: ArrayList<ScheduleSubject>): ArrayList<ScheduleDay> {
        val scheduleDays = ArrayList<ScheduleDay>()

        val scheduleSubjects = subjects.map { it } as ArrayList<ScheduleSubject>

        scheduleSubjects.map { subject ->
            if (subject.dateLesson == null) return@map
            val calendarDate = CalendarDate(subject.dateLesson)
            val schedule = ArrayList<ScheduleSubject>()
            schedule.add(subject)
            scheduleSubjects.remove(subject)
            scheduleSubjects.forEach { subjectInner ->
                if (subject.dateLesson == subjectInner.dateLesson) {
                    schedule.add(subjectInner)
                }
            }
            scheduleDays.add(ScheduleDay(
                id = -1,
                date = calendarDate.getIncDate(0),
                weekDayName = calendarDate.getWeekDayName(),
                weekDayNumber = calendarDate.getWeekDayNumber(),
                lessonsAmount = schedule.size,
                schedule = schedule
            ))
        }

        return scheduleDays
    }

}


