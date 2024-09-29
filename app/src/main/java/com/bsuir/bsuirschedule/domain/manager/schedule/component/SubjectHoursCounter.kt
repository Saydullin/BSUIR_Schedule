package com.bsuir.bsuirschedule.domain.manager.schedule.component

import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleSubjectHours
import kotlin.collections.ArrayList

class SubjectHoursCounter {

    private val hoursPerSubject = 2

    fun execute(
        scheduleDays: ArrayList<ScheduleDay>
    ): ArrayList<ScheduleDay> {
        val subjectsList = scheduleDays.flatMap { it.schedule } as ArrayList<ScheduleSubject>
        val uniqueSubjects = subjectsList.distinctBy { it.getLessonTypeHasCode() }

        uniqueSubjects.map { uniqueSubject ->
            val matchedSubjects = subjectsList.filter {
                uniqueSubject.getLessonTypeHasCode() == it.getLessonTypeHasCode()
            }
            val totalHours = matchedSubjects.size * hoursPerSubject
            matchedSubjects.mapIndexed { index, matchedSubject ->
                val subjectHours = ScheduleSubjectHours(
                    past = (index + 1) * hoursPerSubject,
                    total = totalHours
                )
                matchedSubject.hours = subjectHours
            }
        }

        return scheduleDays
    }

}


