package com.example.bsuirschedule.domain.usecase

import android.util.Log
import com.example.bsuirschedule.domain.models.ScheduleDay
import com.example.bsuirschedule.domain.models.ScheduleSubject
import com.example.bsuirschedule.domain.models.SubjectBreakTime
import com.example.bsuirschedule.domain.utils.CalendarDate

class FullExamsScheduleUseCase {

    fun getSchedule(subjectsList: ArrayList<ScheduleSubject>): ArrayList<ScheduleDay> {
        val scheduleDays = ArrayList<ScheduleDay>()
        val subjects = subjectsList.toMutableList()

        try {
            subjects.map { subject ->
                if (subject.dateLesson == null) return@map // TODO set undefined subject date
                val calendarDate = CalendarDate(subject.dateLesson)
                val subjectsDay = ArrayList<ScheduleSubject>()
                subjectsDay.add(subject)
                subjects.forEach { subjectInner ->
                    if (subject.dateLesson == subjectInner.dateLesson) {
                        val subjectPrev = subjectsDay[subjectsDay.size - 1]
                        subject.breakTime = calendarDate.getSubjectBreakTime(
                            subjectInner.startLessonTime,
                            subjectPrev.endLessonTime
                        )
                        subjectsDay.add(subjectInner)
                    }
                }
                scheduleDays.add(ScheduleDay(
                    date = calendarDate.getIncDate(0),
                    weekDayNumber = calendarDate.getWeekDayNumber(),
                    weekDayName = calendarDate.getWeekDayName(),
                    lessonsAmount = subjectsDay.size,
                    schedule = subjectsDay
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("sady", "ERROR OCCURRED")
        }

        return scheduleDays
    }

}


