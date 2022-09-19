package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.utils.ScheduleManager

class FullExamsScheduleUseCase {

    fun getSchedule(subjectsList: ArrayList<ScheduleSubject>, startDate: String, endDate: String): ArrayList<ScheduleDay> {
        val sm = ScheduleManager()
        val subjects = subjectsList.toMutableList() as ArrayList<ScheduleSubject>

        return sm.getSubjectDays(subjects, startDate, endDate)
    }

}


