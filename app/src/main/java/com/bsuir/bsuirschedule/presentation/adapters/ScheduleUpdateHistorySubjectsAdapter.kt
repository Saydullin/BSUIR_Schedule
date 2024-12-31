package com.bsuir.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleSubjectUpdateHistoryBinding
import com.bsuir.bsuirschedule.domain.models.*
import kotlin.math.absoluteValue

class ScheduleUpdateHistorySubjectsAdapter(
    private val context: Context,
    private val data: ArrayList<ScheduleSubjectHistory>,
    private val isGroupSubject: Boolean,
    private val onClick: ((subject: ScheduleSubjectHistory) -> Unit)?,
): RecyclerView.Adapter<ScheduleUpdateHistorySubjectsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ScheduleSubjectUpdateHistoryBinding.inflate(LayoutInflater.from(context), parent, false)

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
        private val onClick: ((subject: ScheduleSubjectHistory) -> Unit)?,
        private val binding: ScheduleSubjectUpdateHistoryBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(subjectHistory: ScheduleSubjectHistory) {
            val subject = subjectHistory.scheduleSubject
            if (subject.getEditedOrNumSubgroup() != 0) {
                binding.subgroupInfo.visibility = View.VISIBLE
                binding.subjectSubgroup.text = subject.getEditedOrNumSubgroup().toString()
            }
            binding.subjectStartLesson.text = subject.startLessonTime
            binding.subjectEndLesson.text = subject.endLessonTime
            binding.subjectAudience.text = subject.getEditedOrAudienceInLine()
            binding.subjectTitle.text = subject.getEditedOrShortTitle()

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

            when (subjectHistory.status) {
                SubjectHistoryStatus.ADDED -> {
                    binding.subjectUpdateStatusText.text = context.getString(R.string.added)
                    binding.subjectUpdateStatus.setBackgroundResource(R.drawable.subject_update_added_holder)
                    binding.subjectUpdateStatusText.setTextColor(ContextCompat.getColor(context, R.color.success))
                }
                SubjectHistoryStatus.DELETED -> {
                    binding.subjectUpdateStatusText.text = context.getString(R.string.deleted)
                    binding.subjectUpdateStatus.setBackgroundResource(R.drawable.subject_update_deleted_holder)
                    binding.subjectUpdateStatusText.setTextColor(ContextCompat.getColor(context, R.color.danger))
                }
                SubjectHistoryStatus.NOTHING -> {
                    binding.subjectUpdateStatusText.text = context.getString(R.string.no_changes)
                    binding.subjectUpdateStatus.setBackgroundResource(R.drawable.subject_update_empty_holder)
                    binding.subjectUpdateStatusText.setTextColor(ContextCompat.getColor(context, R.color.dark))
                }
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

            if (subject.getEditedOrNote().isNotEmpty()) {
                binding.subjectAdditional.visibility = View.VISIBLE
                binding.subjectNote.text = subject.getEditedOrNote()
            }

            if (onClick != null) {
                binding.root.setOnClickListener {
                    onClick.invoke(subjectHistory)
                }
            }
        }

    }

}


