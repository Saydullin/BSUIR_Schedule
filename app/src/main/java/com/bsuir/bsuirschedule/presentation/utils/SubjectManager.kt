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
        val calendarStart = Calendar.getInstance()
        val millisLeft = subject.startMillis - calendarStart.timeInMillis + 60_000
        val hours = millisLeft / 3_600_000
        val minutes = millisLeft % 3_600_000 / 60_000
        val pluralHoursText = context.resources.getQuantityString(R.plurals.plural_hours, hours.toInt(), hours.toInt())
        val pluralMinutesText = context.resources.getQuantityString(R.plurals.plural_minutes, minutes.toInt(), minutes.toInt())
        val subjectStatusTodayString = if (subject.getAudienceInLine().isEmpty()) {
            R.string.subject_status_today_no_audience
        } else {
            R.string.subject_status_today
        }

        if (hours == 0L) {
            return context.resources.getString(
                subjectStatusTodayString,
                pluralMinutesText,
                subject.subject,
                subject.getAudienceInLine()
            )
        }

        if (minutes == 0L) {
            return context.resources.getString(
                subjectStatusTodayString,
                pluralHoursText,
                subject.subject,
                subject.getAudienceInLine()
            )
        }

        return context.resources.getString(
            subjectStatusTodayString,
            "$pluralHoursText $pluralMinutesText",
            subject.subject,
            subject.getAudienceInLine()
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
        val daysLeft = (subject.startMillis - calendarStart.timeInMillis) / 86_400_000
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

    fun getSubjectType(): String {

        return when (subject.lessonTypeAbbrev) {
            ScheduleSubject.LABORATORY -> {
                context.getString(R.string.laboratory)
            }
            ScheduleSubject.PRACTISE -> {
                context.getString(R.string.practise)
            }
            ScheduleSubject.LECTURE -> {
                context.getString(R.string.lecture)
            }
            ScheduleSubject.CONSULTATION -> {
                context.getString(R.string.consultation)
            }
            ScheduleSubject.EXAM -> {
                context.getString(R.string.exam)
            }
            else -> {
                context.getString(R.string.unknown)
            }
        }
    }

    fun getShortDayOfWeek(): String {
        val timeFormat = SimpleDateFormat("EEE")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = subject.startMillis

        return timeFormat.format(calendar.time)
    }

    fun getSubjectWeeks(): String {

        return context.getString(
            R.string.subject_weeks,
            subject.weekNumber?.joinToString(", ") ?: ""
        )
    }

    fun getSubjectNote(): String {

        return context.getString(
            R.string.subject_note,
            subject.note
        )
    }

    fun getSubjectSubgroup(): String {

        return if (subject.numSubgroup == 0) {
            context.getString(R.string.all_subgroups)
        } else {
            context.getString(R.string.subgroup, subject.numSubgroup)
        }
    }

}


