package com.bsuir.bsuirschedule.presentation.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val onClickSubjectSource: ((savedSchedule: SavedSchedule) -> Unit)?,
): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SubjectDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        val subjectManager = SubjectManager(subject = subject, context = context!!)

        Log.e("sady", "subject startMillis ${subject.startMillis}, on ${subject.dateLesson}")

        val timeText = resources.getString(R.string.subject_time, subject.startLessonTime, subject.endLessonTime)
        binding.subjectTime.text = timeText

        val weekText = resources.getString(R.string.subject_weeks, subject.weekNumber?.joinToString(", ") ?: "")
        binding.subjectWeeks.text = weekText

        binding.subjectTypeName.text = subjectManager.getSubjectType()
        subjectManager.setSubjectTypeView(binding.subjectType)

        if (subject.startLessonDate.isNullOrEmpty() || subject.endLessonDate.isNullOrEmpty()) {
            binding.subjectDateRangeContainer.visibility = View.GONE
        } else {
            binding.subjectDateRangeContainer.visibility = View.VISIBLE
            val subjectStartDateText = subjectManager.getSubjectTextDate(subject.startLessonDate)
            val subjectEndDateText = subjectManager.getSubjectTextDate(subject.endLessonDate)
            val subjectDateRangeText = getString(R.string.subject_date_range, subjectStartDateText, subjectEndDateText)
            binding.subjectDateRange.text = subjectDateRangeText
        }

        binding.subjectSubgroup.text = subjectManager.getSubjectSubgroup()

        if (subject.note != null && subject.note!!.isNotEmpty()) {
            binding.subjectNoteContainer.visibility = View.VISIBLE
            binding.subjectNote.text = subject.getSubjectNote()
        }

        binding.subjectTitle.text = subject.getFullTitle()
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
                    subject.getShortTitle(),
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
                    subject.getShortTitle(),
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
            onClickSubjectSource?.let { it(savedSchedule) }
            dismiss()
        }

        if (subject.employees != null && !subject.employees.isNullOrEmpty()) {
            val scheduleItems = subject.employees!!.map { it.toSavedSchedule() } as ArrayList<SavedSchedule>
            val adapter = SubjectItemsAdapter(context!!, scheduleItems, onClickSource)
            binding.sourceRecycler.adapter = adapter
        }

        if (subject.groups != null && !subject.groups.isNullOrEmpty()) { // TODO: Make here dynamic boolean for exams
            val scheduleItems = subject.groups!!.map { it.toSavedSchedule(false) } as ArrayList<SavedSchedule>
            val adapter = SubjectItemsAdapter(context!!, scheduleItems, onClickSource)
            binding.sourceRecycler.adapter = adapter
        }

        return binding.root
    }

}


