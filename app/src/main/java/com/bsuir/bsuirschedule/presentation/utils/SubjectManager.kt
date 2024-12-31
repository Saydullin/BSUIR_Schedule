package com.bsuir.bsuirschedule.presentation.utils

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
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
        if (subject.getEditedOrAudienceInLine().isEmpty()) {
            return context.resources.getString(
                R.string.subject_status_now,
                subject.getEditedOrShortTitle(),
                subject.getEditedOrAudienceInLine()
            )
        }

        return context.resources.getString(
            R.string.subject_status_now_in,
            subject.getEditedOrShortTitle(),
            subject.getEditedOrAudienceInLine()
        )
    }

    private fun getTodayString(): String {
        val calendarStart = Calendar.getInstance()
        val millisLeft = subject.startMillis - calendarStart.timeInMillis + 60_000
        val hours = millisLeft / 3_600_000
        val minutes = millisLeft % 3_600_000 / 60_000
        val pluralHoursText = context.getString(R.string.short_hour, hours.toInt())
        val pluralMinutesText = context.getString(R.string.short_minute, minutes.toInt())
        val subjectStatusTodayString = if (subject.getEditedOrAudienceInLine().isEmpty()) {
            R.string.subject_status_today_no_audience
        } else {
            R.string.subject_status_today
        }

        if (hours == 0L) {
            return context.resources.getString(
                subjectStatusTodayString,
                pluralMinutesText,
                subject.getEditedOrShortTitle(),
                subject.getEditedOrAudienceInLine()
            )
        }

        if (minutes == 0L) {
            return context.resources.getString(
                subjectStatusTodayString,
                pluralHoursText,
                subject.getEditedOrShortTitle(),
                subject.getEditedOrAudienceInLine()
            )
        }

        return context.resources.getString(
            subjectStatusTodayString,
            "$pluralHoursText $pluralMinutesText",
            subject.getEditedOrShortTitle(),
            subject.getEditedOrAudienceInLine()
        )

    }

    private fun getTomorrowString(): String {

        return context.resources.getString(
            R.string.subject_status_tomorrow,
            getSubjectStartTime(),
            subject.getEditedOrShortTitle()
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
            subject.getEditedOrShortTitle(),
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

    fun setSubjectTypeView(
        imageView: ImageView,
        subjectType: String,
    ) {
        when (subjectType) {
            ScheduleSubject.LESSON_TYPE_LABORATORY -> {
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.subject_lab))
            }
            ScheduleSubject.LESSON_TYPE_PRACTISE -> {
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.subject_practise))
            }
            ScheduleSubject.LESSON_TYPE_LECTURE -> {
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.subject_lecture))
            }
            ScheduleSubject.LESSON_TYPE_PRACTISE2 -> {
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.subject_practise))
            }
            ScheduleSubject.LESSON_TYPE_LECTURE2 -> {
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.subject_lecture))
            }
            ScheduleSubject.LESSON_TYPE_CONSULTATION -> {
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_flag))
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.subject_consultation))
            }
            ScheduleSubject.LESSON_TYPE_PRETEST -> {
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_flag))
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.subject_pretest))
            }
            ScheduleSubject.LESSON_TYPE_EXAM -> {
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_flag))
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.subject_exam))
            }
            else -> {
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.subject_unknown))
            }
        }
    }

    fun getSubjectType(subjectType: String): String {

        return when (subjectType) {
            ScheduleSubject.LESSON_TYPE_LABORATORY -> {
                context.getString(R.string.laboratory)
            }
            ScheduleSubject.LESSON_TYPE_PRACTISE -> {
                context.getString(R.string.practise)
            }
            ScheduleSubject.LESSON_TYPE_PRACTISE2 -> {
                context.getString(R.string.practise)
            }
            ScheduleSubject.LESSON_TYPE_LECTURE -> {
                context.getString(R.string.lecture)
            }
            ScheduleSubject.LESSON_TYPE_LECTURE2 -> {
                context.getString(R.string.lecture)
            }
            ScheduleSubject.LESSON_TYPE_CONSULTATION -> {
                context.getString(R.string.consultation)
            }
            ScheduleSubject.LESSON_TYPE_EXAM -> {
                context.getString(R.string.exam)
            }
            ScheduleSubject.LESSON_TYPE_PRETEST -> {
                context.getString(R.string.pretest)
            }
            else -> {
                subject.lessonTypeAbbrev ?: context.getString(R.string.unknown)
            }
        }
    }

    fun getDayOfWeek(): String {
        val timeFormat = SimpleDateFormat("EEEE")
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

    fun getSubjectSubgroup(): String {

        return if (subject.getEditedOrNumSubgroup() == 0) {
            context.getString(R.string.all_subgroups)
        } else {
            context.getString(R.string.subgroup, subject.getEditedOrNumSubgroup())
        }
    }

    fun getSubjectTextDate(datePattern: String?): String? {
        if (datePattern.isNullOrEmpty()) return null
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val dateTextFormat = SimpleDateFormat("d MMMM")
        val datePatternTime = dateFormat.parse(datePattern)
        if (datePatternTime != null) {
            return dateTextFormat.format(datePatternTime.time)
        }

        return null
    }

}


