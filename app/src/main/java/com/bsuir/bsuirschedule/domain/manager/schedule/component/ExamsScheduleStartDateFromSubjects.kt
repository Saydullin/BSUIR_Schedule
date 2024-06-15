package com.bsuir.bsuirschedule.domain.manager.schedule.component

import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import java.text.SimpleDateFormat

class ExamsScheduleStartDateFromSubjects(
    private val scheduleSubjects: ArrayList<ScheduleSubject>,
) {

    fun execute(): String? {
        val firstSubject = scheduleSubjects.maxByOrNull { subject ->
            if (!subject.dateLesson.isNullOrEmpty()) {
                val inputFormat = SimpleDateFormat("dd.MM.yyyy")
                val parsedDate = (inputFormat.parse(subject.dateLesson)?.time ?: 0) * -1
                parsedDate
            } else {
                0
            }
        }

        return firstSubject?.dateLesson
    }

}


