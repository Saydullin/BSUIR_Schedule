package com.bsuir.bsuirschedule.presentation.dialogs

import android.app.Dialog
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

class SubjectDialog(private val subject: ScheduleSubject): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SubjectDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)

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

        if (subject.numSubgroup == 0) {
            binding.subjectSubgroup.text = resources.getString(R.string.all_subgroups)
        } else {
            val subgroupText = resources.getString(R.string.subgroup, subject.numSubgroup)
            binding.subjectSubgroup.text = subgroupText
        }

        if (subject.note != null && subject.note.isNotEmpty()) {
            val noteText = resources.getString(R.string.subject_note, subject.note)
            binding.subjectNote.visibility = View.VISIBLE
            binding.subjectNote.text = noteText
        }

        binding.subjectTitle.text = subject.subjectFullName
        binding.subjectAudience.text = subject.getAudienceInLine()
        binding.sourceRecycler.layoutManager = LinearLayoutManager(context)

        if (subject.employees != null && !subject.employees.isNullOrEmpty()) {
            val scheduleItems = subject.employees!!.map { it.toSavedSchedule() } as ArrayList<SavedSchedule>
            val adapter = SubjectItemsAdapter(context!!, scheduleItems)
            binding.sourceRecycler.adapter = adapter
        }

        if (subject.groups != null && subject.groups!!.isNotEmpty()) {
            val scheduleItems = subject.groups!!.map { it.toSavedSchedule(false) } as ArrayList<SavedSchedule>
            val adapter = SubjectItemsAdapter(context!!, scheduleItems)
            binding.sourceRecycler.adapter = adapter
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setRetainInstance(true)
        return super.onCreateDialog(savedInstanceState)
    }

}


