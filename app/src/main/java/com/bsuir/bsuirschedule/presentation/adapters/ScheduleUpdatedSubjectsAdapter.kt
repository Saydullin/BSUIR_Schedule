package com.bsuir.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleUpdatedSubjectBinding
import com.bsuir.bsuirschedule.domain.models.EmployeeSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.GroupSubject

class ScheduleUpdatedSubjectsAdapter(
    private val context: Context,
    private val data: List<ScheduleSubject>,
    private val isGroupSubject: Boolean,
    private val onClick: ((subject: ScheduleSubject) -> Unit)?,
): RecyclerView.Adapter<ScheduleUpdatedSubjectsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ScheduleUpdatedSubjectBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(context, isGroupSubject, onClick, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val groupScheduleSubject = data[position]

        holder.bind(groupScheduleSubject)
    }

    override fun getItemCount() = data.size

    class ViewHolder(
        private val context: Context,
        private val isGroup: Boolean,
        private val onClick: ((subject: ScheduleSubject) -> Unit)?,
        private val binding: ScheduleUpdatedSubjectBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(subject: ScheduleSubject) {
            if (subject.getEditedOrNumSubgroup() != 0) {
                binding.subgroupInfo.visibility = View.VISIBLE
                binding.subjectSubgroup.text = subject.getEditedOrNumSubgroup().toString()
            }
            binding.subjectStartLesson.text = subject.startLessonTime
            binding.subjectEndLesson.text = subject.endLessonTime
            binding.subjectAudience.text = subject.getEditedOrAudienceInLine()
            binding.subjectTitle.text = subject.getEditedOrShortTitle()
            binding.subjectWeekDay.text = subject.getEditedOrWeekDay().toString()
            binding.subjectWeeks.text = subject.getEditedOrWeeks().joinToString(", ")

            if (subject.isActual == true) {
                binding.actualSubjectIcon.visibility = View.VISIBLE
            } else {
                binding.actualSubjectIcon.visibility = View.GONE
            }

            val imageView = binding.subjectType

            when (subject.lessonTypeAbbrev) {
                ScheduleSubject.LESSON_TYPE_LABORATORY -> {
                    imageView.setColorFilter(ContextCompat.getColor(context, R.color.subject_lab))
                }
                ScheduleSubject.LESSON_TYPE_PRACTISE -> {
                    imageView.setColorFilter(ContextCompat.getColor(context, R.color.subject_practise))
                }
                ScheduleSubject.LESSON_TYPE_LECTURE -> {
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

            if (isGroup) {
                setSubjectEmployee(subject.employees ?: ArrayList())
            } else {
                setSubjectGroup(subject.subjectGroups ?: ArrayList())
            }

            if (subject.isIgnored == true) {
                binding.root.alpha = .7f
            }

            if (subject.getEditedOrNote().isNotEmpty()) {
                binding.subjectNote.visibility = View.VISIBLE
                binding.subjectNote.text = subject.getEditedOrNote()
            }

            if (onClick != null) {
                binding.root.setOnClickListener {
                    onClick.invoke(subject)
                }
            }

        }

        private fun setSubjectEmployee(employees: ArrayList<EmployeeSubject>) {
            if (employees.isNotEmpty()) {
                binding.subjectEmployeeName.text = employees[0].getName() + if (employees.size > 1) {
                    val moreText = context.getString(R.string.more)
                    ", $moreText ${employees.size - 1}"
                } else ""
            } else {
                binding.subjectEmployeeName.text = context.getString(R.string.no_teacher_title)
            }
        }

        private fun setSubjectGroup(subjectGroupList: ArrayList<GroupSubject>) {
            if (subjectGroupList.isNotEmpty()) {
                binding.subjectEmployeeName.text = subjectGroupList[0].name + if (subjectGroupList.size > 1) {
                    val moreText = context.getString(R.string.more)
                    ", $moreText ${subjectGroupList.size - 1}"
                } else ""
            } else {
                binding.subjectEmployeeName.text = context.getString(R.string.no_group_title)
            }
        }

    }

}


