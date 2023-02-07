package com.bsuir.bsuirschedule.domain.utils

import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import java.text.SimpleDateFormat
import java.util.*

class WidgetSubjectController (
    private val schedule: Schedule
) {

    fun getNextCallTime(): Long? {
        val actualSubject = getActualSubject()

        if (actualSubject != null) {
            return actualSubject.endMillis
        } else {
            val nextSubject = getNextSubject()
            if (nextSubject != null) {
                return nextSubject.startMillis
            }
        }

        return null
    }

    private fun getNextSubject(): ScheduleSubject? {
        val actualScheduleDays = getActualScheduleDays()
        val currentMillis = Date().time

        actualScheduleDays.map { scheduleDay ->
            scheduleDay.schedule.map { scheduleSubject ->
                if (scheduleSubject.startMillis >= currentMillis
                    && scheduleSubject.isIgnored != true) {
                    return scheduleSubject
                }
            }
        }

        return null
    }

    private fun getActualSubject(): ScheduleSubject? {
        val actualScheduleDays = getActualScheduleDays()
        val currentMillis = Date().time

        actualScheduleDays.map { scheduleDay ->
            scheduleDay.schedule.map { scheduleSubject ->
                if (scheduleSubject.startMillis <= currentMillis
                    && scheduleSubject.endMillis > currentMillis
                    && scheduleSubject.isIgnored != true) {
                    return scheduleSubject
                }
            }
        }

        return null
    }

    private fun getActualScheduleDays(): List<ScheduleDay> {
        val format = SimpleDateFormat("dd/MM/yyyy")
        val today = Date()
        val todayWithZeroTime = format.parse(format.format(today))!!.time

        return schedule.schedules.filter { it.dateInMillis >= todayWithZeroTime }
    }

    fun getActualScheduleDay(): ScheduleDay? {
        val actualScheduleDays = getActualScheduleDays()
        val currentMillis = Date().time

        val actualScheduleDayIndex = actualScheduleDays.indexOfFirst { scheduleDay ->
            val notIgnoredSubjects = scheduleDay.schedule.filter {
                it.isIgnored != true && it.endMillis > currentMillis
            }
            notIgnoredSubjects.isNotEmpty()
        }

        if (actualScheduleDayIndex != -1) {
            return actualScheduleDays[actualScheduleDayIndex]
        }

        return null
    }

}


