package com.bsuir.bsuirschedule.presentation.utils

import android.content.Context
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject

class SubjectStringsManager(
    private val subject: ScheduleSubject,
    private val context: Context
) {

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


