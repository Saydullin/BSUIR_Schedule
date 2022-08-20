package com.example.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bsuirschedule.R
import com.example.bsuirschedule.databinding.ScheduleSubjectBinding
import com.example.bsuirschedule.domain.models.EmployeeSubject
import com.example.bsuirschedule.domain.models.Group
import com.example.bsuirschedule.domain.models.ScheduleSubject
import com.example.bsuirschedule.domain.models.GroupSubject
import kotlin.math.absoluteValue

class ScheduleSubjectsAdapter(
    private val context: Context,
    private val data: ArrayList<ScheduleSubject>,
    private val isGroupSubject: Boolean,
    private val showSubjectDialog: (subject: ScheduleSubject) -> Unit
): RecyclerView.Adapter<ScheduleSubjectsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ScheduleSubjectBinding.inflate(LayoutInflater.from(context), parent, false)

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
        private val showSubjectDialog: (subject: ScheduleSubject) -> Unit,
        private val binding: ScheduleSubjectBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(subject: ScheduleSubject) {
            if (subject.numSubgroup != 0) {
                binding.subgroupInfo.visibility = View.VISIBLE
                binding.subjectSubgroup.text = subject.numSubgroup.toString()
            }
            binding.subjectStartLesson.text = subject.startLessonTime
            binding.subjectEndLesson.text = subject.endLessonTime
            binding.subjectAudience.text = subject.getAudienceInLine()
            binding.subjectTitle.text = subject.subject

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
                showSubjectDialog(subject)
            }

        }

        private fun setSubjectEmployee(employees: ArrayList<EmployeeSubject>) {
            if (employees.isNotEmpty()) {
                binding.subjectEmployeeName.text = employees[0].getName() + if (employees.size > 1) ", +${employees.size-1}" else ""
                Glide.with(context)
                    .load(employees[0].photoLink)
                    .placeholder(R.drawable.ic_person_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.scheduleImage)
                binding.subjectEmployeeDegree.text = employees[0].getRankAndDegree()
            } else {
                binding.subjectEmployeeName.text = context.getString(R.string.no_teacher_title)
                Glide.with(context)
                    .load(R.drawable.ic_no_teacher_placeholder)
                    .placeholder(R.drawable.ic_person_placeholder)
                    .into(binding.scheduleImage)
            }
        }

        private fun setSubjectGroup(subjectGroupList: ArrayList<GroupSubject>, subjectGroups: ArrayList<Group>) {
            val courseText = context.getString(R.string.course)
            Glide.with(context)
                .load(R.drawable.ic_group_placeholder)
                .into(binding.scheduleImage)
            if (subjectGroupList.isNotEmpty()) {
                binding.subjectEmployeeName.text = subjectGroupList[0].name + if (subjectGroupList.size > 1) ", +${subjectGroupList.size-1}" else ""
            } else {
                binding.subjectEmployeeName.text = context.getString(R.string.no_group_title)
            }
            if (subjectGroups.isNotEmpty()) {
                binding.subjectEmployeeDegree.text = subjectGroups[0].getFacultyAndSpecialityAbbr()
                binding.subjectEmployeeCourse.text = "${subjectGroups[0].course} $courseText"
            }
        }

    }

}


