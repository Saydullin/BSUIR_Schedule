package com.bsuir.bsuirschedule.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.SubjectDialogBinding
import com.bsuir.bsuirschedule.domain.models.EmployeeSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.presentation.adapters.SubjectItemsAdapter
import com.bsuir.bsuirschedule.presentation.utils.SubjectManager
import com.bsuir.bsuirschedule.presentation.utils.ViewVisible

class SubjectDialog(
    private val subject: ScheduleSubject,
    private val onClickSubjectSource: ((
        savedSchedule: SavedSchedule,
        employeeSubject: EmployeeSubject?
    ) -> Unit)?,
    private val scheduleSettings: ScheduleSettings?
): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SubjectDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_bg_shadow)

        val subjectManager = SubjectManager(subject = subject, context = context!!)

        val timeText = resources.getString(
            R.string.subject_time,
            subject.getEditedOrStartTime(),
            subject.getEditedOrEndTime()
        )
        binding.subjectTime.text = timeText

        val weekText = resources.getString(R.string.subject_weeks, subject.weekNumber?.joinToString(", ") ?: "")
        binding.subjectWeeks.text = weekText

        binding.subjectTypeName.text = subjectManager.getSubjectType(subject.getEditedOrLessonType())
        subjectManager.setSubjectTypeView(binding.subjectType, subject.getEditedOrLessonType())

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

        if (subject.getEditedOrNote().isNotEmpty()) {
            binding.subjectNoteContainer.visibility = View.VISIBLE
            binding.subjectNote.text = subject.getEditedOrNote()
        }

        binding.subjectTitle.text = subject.getEditedOrFullTitle()
        binding.subjectAudience.text = subject.getEditedOrAudienceInLine()
        binding.sourceRecycler.layoutManager = LinearLayoutManager(context)

        binding.subjectHoursAmountContainer.visibility = ViewVisible.ifIs(!subject.hours.isNullOrEmpty())
        val forSubgroupText = resources.getString(
            R.string.for_subgroup,
            scheduleSettings?.subgroup?.selectedNum
        )
        val hoursText = resources.getString(
            R.string.subject_hours,
            subject.hours
        )
        binding.subjectHoursAmount.text =
            if (scheduleSettings?.subgroup?.selectedNum != 0 &&
                subject.numSubgroup != 0) {
                "$hoursText $forSubgroupText"
            } else {
                hoursText
            }
//        if (subject.nextTimeDaysLeft != null) {
//            val daysLeft = subject.nextTimeDaysLeft!!
//            val afterText = resources.getString(R.string.after)
//            if (daysLeft >= 7) {
//                val weeksAmount = daysLeft / 7
//                val andDaysLeft = daysLeft % 7
//                var daysLeftText = ""
//                val weeksAmountText = resources.getQuantityString(
//                    R.plurals.plural_weeks_left,
//                    weeksAmount,
//                    weeksAmount
//                )
//                if (andDaysLeft != 0) {
//                    val andText = resources.getString(R.string.and)
//                    daysLeftText = "$andText " + resources.getQuantityString(
//                        R.plurals.plural_days_left,
//                        andDaysLeft,
//                        andDaysLeft
//                    )
//                }
//                val nextTimeSubjectText = resources.getString(
//                    R.string.subject_next_time,
//                    subject.getEditedOrShortTitle(),
//                    afterText
//                )
//                binding.subjectNextTime.text = "$nextTimeSubjectText $weeksAmountText $daysLeftText".trim()
//            } else {
//                val pluralDaysText = resources.getQuantityString(
//                    R.plurals.plural_days_left,
//                    subject.nextTimeDaysLeft!!,
//                    subject.nextTimeDaysLeft
//                )
//                val nextTimeSubjectText = resources.getString(
//                    R.string.subject_next_time,
//                    subject.getEditedOrShortTitle(),
//                    "$afterText $pluralDaysText"
//                )
//                binding.subjectNextTime.text = nextTimeSubjectText
//            }
//        } else {
//            val undefinedNextTimeSubjectText = resources.getString(
//                R.string.subject_next_time_undefined
//            )
//            binding.subjectNextTime.text = undefinedNextTimeSubjectText
//        }

        val onClickSource = { savedSchedule: SavedSchedule ->
            if (!savedSchedule.isGroup) {
                val employee = subject.employees?.find { it.toSavedSchedule() == savedSchedule }
                onClickSubjectSource?.invoke(savedSchedule, employee)
            } else {
                onClickSubjectSource?.invoke(savedSchedule, null)
            }
            dismiss()
        }

        if (subject.employees != null && !subject.employees.isNullOrEmpty()) {
            val scheduleItems = subject.getEditedOrEmployees()
            val adapter = SubjectItemsAdapter(context!!, scheduleItems, onClickSource)
            binding.sourceRecycler.adapter = adapter
        }

        if (subject.groups != null && !subject.groups.isNullOrEmpty()) { // TODO: Make here dynamic boolean for exams
            val scheduleItems = subject.groups?.map { it.toSavedSchedule(false) } as ArrayList<SavedSchedule>
            val adapter = SubjectItemsAdapter(context!!, scheduleItems, onClickSource)
            binding.sourceRecycler.adapter = adapter
        }

        return binding.root
    }

}


