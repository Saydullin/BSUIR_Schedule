package com.bsuir.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleSubjectShortBinding
import com.bsuir.bsuirschedule.domain.models.EmployeeSubject
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.GroupSubject
import kotlin.math.absoluteValue

class ScheduleShortSubjectsAdapter(
    private val context: Context,
    private val data: ArrayList<ScheduleSubject>,
    private val isGroupSubject: Boolean,
    private val showSubjectDialog: ((subject: ScheduleSubject) -> Unit)?
): RecyclerView.Adapter<ScheduleShortSubjectsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ScheduleSubjectShortBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(context, isGroupSubject, showSubjectDialog, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val groupScheduleSubject = data[position]

        holder.bind(groupScheduleSubject)
    }

    override fun getItemCount() = data.size

    class ViewHolder(
        private val context: Context,
        private val isGroup: Boolean,
        private val showSubjectDialog: ((subject: ScheduleSubject) -> Unit)?,
        private val binding: ScheduleSubjectShortBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(subject: ScheduleSubject) {
            if (subject.numSubgroup != 0) {
                binding.subgroupInfo.visibility = View.VISIBLE
                binding.subjectSubgroup.text = subject.getNumSubgroupStr()
            }
            binding.subjectStartLesson.text = subject.startLessonTime
            binding.subjectEndLesson.text = subject.endLessonTime
            binding.subjectAudience.text = subject.getAudienceInLine()
            binding.subjectTitle.text = subject.subject

            if (subject.isActual == true) {
                binding.actualSubjectIcon.visibility = View.VISIBLE
            } else {
                binding.actualSubjectIcon.visibility = View.GONE
            }

            when (subject.lessonTypeAbbrev) {
                ScheduleSubject.LABORATORY -> {
                    binding.subjectType.setColorFilter(ContextCompat.getColor(context, R.color.subject_lab))
                }
                ScheduleSubject.PRACTISE -> {
                    binding.subjectType.setColorFilter(ContextCompat.getColor(context, R.color.subject_practise))
                }
                ScheduleSubject.LECTURE -> {
                    binding.subjectType.setColorFilter(ContextCompat.getColor(context, R.color.subject_lecture))
                }
                ScheduleSubject.CONSULTATION -> {
                    binding.subjectType.setColorFilter(ContextCompat.getColor(context, R.color.subject_consultation))
                }
                ScheduleSubject.EXAM -> {
                    binding.subjectType.setColorFilter(ContextCompat.getColor(context, R.color.subject_exam))
                }
            }

            if (isGroup) {
                setSubjectEmployee(subject.employees ?: ArrayList())
            } else {
                setSubjectGroup(subject.subjectGroups ?: ArrayList(), subject.groups ?: ArrayList())
            }

            if (subject.breakTime?.isExist == true) {
                val breakTime = subject.breakTime!!
                val breakTimeMinutes = context.resources.getQuantityString(R.plurals.plural_minutes, breakTime.minutes.absoluteValue, breakTime.minutes)
                val breakTimeHours = context.resources.getQuantityString(R.plurals.plural_hours, breakTime.hours.absoluteValue, breakTime.hours)
                if (breakTime.hours != 0) {
                    binding.subjectBreakTime.text = "$breakTimeHours $breakTimeMinutes"
                } else if (breakTime.minutes != 0) {
                    binding.subjectBreakTime.text = breakTimeMinutes
                } else {
                    binding.subjectBreakTime.text = context.getString(R.string.subject_break_time_or)
                }
            } else {
                binding.subjectBreakTime.visibility = View.GONE
            }

            if (subject.note?.isNotEmpty() == true) {
                binding.subjectAdditional.visibility = View.VISIBLE
                binding.subjectNote.text = subject.note
            }

            binding.root.setOnClickListener {
                showSubjectDialog?.let { it(subject) }
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

        private fun setSubjectGroup(subjectGroupList: ArrayList<GroupSubject>, subjectGroups: ArrayList<Group>) {
            val courseText = context.getString(R.string.course)
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


