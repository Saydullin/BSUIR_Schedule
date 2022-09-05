package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.utils.ScheduleManager

class FullExamsScheduleUseCase {

    fun getSchedule(subjectsList: ArrayList<ScheduleSubject>, startDate: String, endDate: String): ArrayList<ScheduleDay> {
        val scheduleDays = ArrayList<ScheduleDay>()
        val subjects = subjectsList.toMutableList()
        val sm = ScheduleManager()

        return sm.getSubjectDays(subjectsList, startDate, endDate)

//        try {
//            subjects.map { subject ->
//                if (subject.dateLesson == null) return@map // TODO set undefined subject date
//                val calendarDate = CalendarDate(subject.dateLesson)
//                val subjectsDay = ArrayList<ScheduleSubject>()
//                subjectsDay.add(subject)
//                subjects.forEach { subjectInner ->
//                    if (subject.dateLesson == subjectInner.dateLesson) {
//                        val subjectPrev = subjectsDay[subjectsDay.size - 1]
//                        subject.breakTime = calendarDate.getSubjectBreakTime(
//                            subjectInner.startLessonTime,
//                            subjectPrev.endLessonTime
//                        )
//                        subjectsDay.add(subjectInner)
//                    }
//                }
//                scheduleDays.add(ScheduleDay(
//                    date = calendarDate.getIncDate(0),
//                    weekDayNumber = calendarDate.getWeekDayNumber(),
//                    weekDayName = calendarDate.getWeekDayName(),
//                    lessonsAmount = subjectsDay.size,
//                    schedule = subjectsDay
//                ))
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e("sady", "ERROR OCCURRED")
//        }

        return scheduleDays
    }

}


