package com.bsuir.bsuirschedule.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.SubjectDialogBinding
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.presentation.adapters.SubjectItemsAdapter
import com.bsuir.bsuirschedule.presentation.utils.SubjectManager

class SubjectDialog(
    private val subject: ScheduleSubject,
    private val onClickSubjectSource: (savedSchedule: SavedSchedule) -> Unit,
): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SubjectDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        val subjectManager = SubjectManager(subject = subject, context = context!!)

        val timeText = resources.getString(R.string.subject_time, subject.startLessonTime, subject.endLessonTime)
        binding.subjectTime.text = timeText

        val weekText = resources.getString(R.string.subject_weeks, subject.weekNumber?.joinToString(", ") ?: "")
        binding.subjectWeeks.text = weekText

        when (subject.lessonTypeAbbrev) {
            ScheduleSubject.LABORATORY -> {
                binding.subjectType.setColorFilter(ContextCompat.getColor(context!!, R.color.subject_lab))
                binding.subjectTypeName.text = resources.getString(R.string.laboratory)
            }
            ScheduleSubject.PRACTISE -> {
                binding.subjectType.setColorFilter(ContextCompat.getColor(context!!, R.color.subject_practise))
                binding.subjectTypeName.text = resources.getString(R.string.practise)
            }
            ScheduleSubject.LECTURE -> {
                binding.subjectType.setColorFilter(ContextCompat.getColor(context!!, R.color.subject_lecture))
                binding.subjectTypeName.text = resources.getString(R.string.lecture)
            }
            ScheduleSubject.CONSULTATION -> {
                binding.subjectType.setColorFilter(ContextCompat.getColor(context!!, R.color.subject_consultation))
                binding.subjectTypeName.text = resources.getString(R.string.consultation)
            }
            ScheduleSubject.EXAM -> {
                binding.subjectType.setColorFilter(ContextCompat.getColor(context!!, R.color.subject_exam))
                binding.subjectTypeName.text = resources.getString(R.string.exam)
            }
        }

        binding.subjectSubgroup.text = subjectManager.getSubjectSubgroup()

        if (subject.note != null && subject.note.isNotEmpty()) {
            binding.subjectNote.visibility = View.VISIBLE
            binding.subjectNote.text = subjectManager.getSubjectNote()
        }

        binding.subjectTitle.text = subject.subjectFullName
        binding.subjectAudience.text = subject.getAudienceInLine()
        binding.sourceRecycler.layoutManager = LinearLayoutManager(context)

        if (subject.nextTimeDaysLeft != null) {
            val daysLeft = subject.nextTimeDaysLeft!!
            val afterText = resources.getString(R.string.after)
            if (daysLeft >= 7) {
                val weeksAmount = daysLeft / 7
                val andDaysLeft = daysLeft % 7
                var daysLeftText = ""
                val weeksAmountText = resources.getQuantityString(
                    R.plurals.plural_weeks_left,
                    weeksAmount,
                    weeksAmount
                )
                if (andDaysLeft != 0) {
                    val andText = resources.getString(R.string.and)
                    daysLeftText = "$andText " + resources.getQuantityString(
                        R.plurals.plural_days_left,
                        andDaysLeft,
                        andDaysLeft
                    )
                }
                val nextTimeSubjectText = resources.getString(
                    R.string.subject_next_time,
                    subject.subject,
                    afterText
                )
                binding.subjectNextTime.text = "$nextTimeSubjectText $weeksAmountText $daysLeftText".trim()
            } else {
                val pluralDaysText = resources.getQuantityString(
                    R.plurals.plural_days_left,
                    subject.nextTimeDaysLeft!!,
                    subject.nextTimeDaysLeft
                )
                val nextTimeSubjectText = resources.getString(
                    R.string.subject_next_time,
                    subject.subject,
                    "$afterText $pluralDaysText"
                )
                binding.subjectNextTime.text = nextTimeSubjectText
            }
        } else {
            val undefinedNextTimeSubjectText = resources.getString(
                R.string.subject_next_time_undefined
            )
            binding.subjectNextTime.text = undefinedNextTimeSubjectText
        }

        val onClickSource = { savedSchedule: SavedSchedule ->
            onClickSubjectSource(savedSchedule)
            dismiss()
        }

        if (subject.employees != null && !subject.employees.isNullOrEmpty()) {
            val scheduleItems = subject.employees!!.map { it.toSavedSchedule() } as ArrayList<SavedSchedule>
            val adapter = SubjectItemsAdapter(context!!, scheduleItems, onClickSource)
            binding.sourceRecycler.adapter = adapter
        }

        if (subject.groups != null && subject.groups!!.isNotEmpty()) {
            val scheduleItems = subject.groups!!.map { it.toSavedSchedule(false) } as ArrayList<SavedSchedule>
            val adapter = SubjectItemsAdapter(context!!, scheduleItems, onClickSource)
            binding.sourceRecycler.adapter = adapter
        }

        return binding.root
    }

}


