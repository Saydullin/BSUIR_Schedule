package com.bsuir.bsuirschedule.domain.usecase

import android.util.Log
import com.bsuir.bsuirschedule.domain.models.GroupSchedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.utils.ScheduleManager

class FullExamsScheduleUseCase {

    fun getSchedule(groupSchedule: GroupSchedule): ArrayList<ScheduleDay> {
        val sm = ScheduleManager()
        val exams = groupSchedule.exams ?: ArrayList()
        val startDate = groupSchedule.startDate
        val endDate = groupSchedule.endDate

        if (exams.isEmpty() || startDate.isNullOrEmpty() || endDate.isNullOrEmpty()) {
            return ArrayList()
        }

        val subjects = exams.toMutableList() as ArrayList<ScheduleSubject>

        return sm.getSubjectDays(subjects, startDate, endDate)
    }

}


