package com.example.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bsuirschedule.R
import com.example.bsuirschedule.databinding.ScheduleDayBinding
import com.example.bsuirschedule.domain.models.ScheduleSubject
import com.example.bsuirschedule.domain.models.ScheduleDay

class MainScheduleAdapter(
    private val context: Context,
    private var data: ArrayList<ScheduleDay>,
    var isGroupSchedule: Boolean,
    private val showSubjectDialog: (subject: ScheduleSubject) -> Unit,
    private val isExams: Boolean = false
): RecyclerView.Adapter<MainScheduleAdapter.ViewHolder>() {

    fun updateSchedule(newData: ArrayList<ScheduleDay>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ScheduleDayBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(isGroupSchedule, showSubjectDialog, view, isExams)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scheduleDay = data[position]
        holder.bind(scheduleDay, context)
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun getItemCount() = data.size

    class ViewHolder(
        private val isGroupSchedule: Boolean,
        private val showSubjectDialog: (subject: ScheduleSubject) -> Unit,
        private val binding: ScheduleDayBinding,
        private val isExams: Boolean
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(scheduleDay: ScheduleDay, context: Context) {
            val lessonsText = if (isExams) {
                context.resources.getQuantityString(
                    R.plurals.plural_exams,
                    scheduleDay.lessonsAmount,
                    scheduleDay.lessonsAmount
                )
            } else {
                context.resources.getQuantityString(
                    R.plurals.plural_lessons,
                    scheduleDay.lessonsAmount,
                    scheduleDay.lessonsAmount
                )
            }

            binding.scheduleDate.text = scheduleDay.date
            binding.scheduleWeekDay.text = scheduleDay.weekDayName.replaceFirstChar { it.uppercase() }
            binding.scheduleLessonsAmount.text = lessonsText
            if (scheduleDay.schedule.isEmpty()) {
                val textColor = getColor(context, R.color.text)
                binding.scheduleSubjectsRecycler.visibility = View.GONE
                binding.scheduleNoLessons.visibility = View.VISIBLE
                binding.scheduleDayHeader.setBackgroundResource(R.drawable.subject_empty_holder)
                binding.scheduleDate.setTextColor(textColor)
                binding.scheduleWeekDay.setTextColor(textColor)
                binding.scheduleLessonsAmount.setTextColor(textColor)
            } else {
                if (isExams) {
                    binding.scheduleDayHeader.setBackgroundResource(R.drawable.subject_exams_holder)
                }
                val viewPool = RecyclerView.RecycledViewPool()
                val adapter = ScheduleSubjectsAdapter(context, scheduleDay.schedule, isGroupSchedule, showSubjectDialog)
                binding.scheduleSubjectsRecycler.adapter = adapter
                binding.scheduleSubjectsRecycler.layoutManager = LinearLayoutManager(context)
                binding.scheduleSubjectsRecycler.setRecycledViewPool(viewPool)
                binding.scheduleWeekDay.text = scheduleDay.weekDayName.replaceFirstChar { it.uppercase() }
            }

        }

    }

}


