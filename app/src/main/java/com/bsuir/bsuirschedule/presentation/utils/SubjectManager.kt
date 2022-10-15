package com.bsuir.bsuirschedule.presentation.utils

import android.content.Context
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import java.text.SimpleDateFormat
import java.util.*

class SubjectManager(
    private val subject: ScheduleSubject,
    private val context: Context
) {

    private fun getSubjectStartTime(): String {
        val timeFormat = SimpleDateFormat("k:mm")
        val calendarStart = Calendar.getInstance()
        calendarStart.timeInMillis = subject.startMillis

        return timeFormat.format(calendarStart.time)
    }

    private fun getNowString(): String {
        if (subject.audience.isNullOrEmpty()) {
            return context.resources.getString(
                R.string.subject_status_now,
                subject.subject,
                subject.getAudienceInLine()
            )
        }

        return context.resources.getString(
            R.string.subject_status_now_in,
            subject.subject,
            subject.getAudienceInLine()
        )
    }

    private fun getTodayString(): String {

        return context.resources.getString(
            R.string.subject_status_today,
            getSubjectStartTime(),
            subject.subject
        )
    }

    private fun getTomorrowString(): String {

        return context.resources.getString(
            R.string.subject_status_tomorrow,
            getSubjectStartTime(),
            subject.subject
        )
    }

    private fun getDaysLeftString(): String {
        val calendarStart = Calendar.getInstance()
        val daysLeft = (subject.startMillis - calendarStart.timeInMillis) / 86400000
        val daysText = context.resources.getQuantityString(
            R.plurals.plural_days_left,
            daysLeft.toInt(),
            daysLeft.toInt()
        )

        return context.resources.getString(
            R.string.subject_status_days_left,
            daysText,
            subject.subject,
            subject.startLessonTime
        )
    }

    fun getSubjectDate(): String {
        val timeFormat = SimpleDateFormat("dd.MM.yyyy")
        val currCalendar = Calendar.getInstance()
        val subjectCalendar = Calendar.getInstance()
        subjectCalendar.timeInMillis = subject.startMillis
        val subjectDay = timeFormat.format(subjectCalendar.time)

        // Now
        if (subject.startMillis < currCalendar.timeInMillis && subject.endMillis > currCalendar.timeInMillis) {
            return getNowString()
        }
        // Today
        if (timeFormat.format(currCalendar.time) == subjectDay) {
            return getTodayString()
        }
        // Tomorrow
        currCalendar.add(Calendar.DATE, 1)
        if (timeFormat.format(currCalendar.time) == subjectDay) {
            return getTomorrowString()
        }

        // N days left
        return getDaysLeftString()
    }

}


