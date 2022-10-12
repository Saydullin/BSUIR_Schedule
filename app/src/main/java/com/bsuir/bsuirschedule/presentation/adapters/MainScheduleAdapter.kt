package com.bsuir.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleDayBinding
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.utils.CalendarDate

class MainScheduleAdapter(
    private val context: Context,
    private var data: ArrayList<ScheduleDay>,
    private var isGroupSchedule: Boolean,
    private var showSubjectDialog: ((subject: ScheduleSubject) -> Unit)?,
    private val isExams: Boolean = false
): RecyclerView.Adapter<MainScheduleAdapter.ViewHolder>() {

    fun updateSchedule(newData: ArrayList<ScheduleDay>, isGroup: Boolean, subjectDialog: ((subject: ScheduleSubject) -> Unit)?) {
        isGroupSchedule = isGroup
        data.clear()
        data.addAll(newData)
        showSubjectDialog = subjectDialog
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ScheduleDayBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(isGroupSchedule, showSubjectDialog, view, isExams)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scheduleDay = data[position]
        holder.bind(scheduleDay, context, position)
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun getItemCount() = data.size

    class ViewHolder(
        private val isGroupSchedule: Boolean,
        private val showSubjectDialog: ((subject: ScheduleSubject) -> Unit)?,
        private val binding: ScheduleDayBinding,
        private val isExams: Boolean
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(scheduleDay: ScheduleDay, context: Context, position: Int) {
            val lessonsText = if (isExams) {
                context.resources.getQuantityString(
                    R.plurals.plural_exams,
                    scheduleDay.schedule.size,
                    scheduleDay.schedule.size
                )
            } else {
                context.resources.getQuantityString(
                    R.plurals.plural_lessons,
                    scheduleDay.schedule.size,
                    scheduleDay.schedule.size
                )
            }

            when (scheduleDay.date) {
                CalendarDate.TODAY -> {
                    binding.scheduleDate.text = context.getString(R.string.today)
                }
                CalendarDate.TOMORROW -> {
                    binding.scheduleDate.text = context.getString(R.string.tomorrow)
                }
                else -> {
                    binding.scheduleDate.text = scheduleDay.date
                }
            }
            if (scheduleDay.weekDayNumber == 1 && position != 0) {
                binding.weekLayout.visibility = View.VISIBLE
                binding.weekNumber.text = context.getString(R.string.schedule_week_number, scheduleDay.weekNumber)
            }
            binding.weekNumberDigit.text = scheduleDay.weekNumberString()
            binding.scheduleWeekDay.text = scheduleDay.weekDayNameUpperFirstLetter()
            binding.scheduleLessonsAmount.text = lessonsText
            if (scheduleDay.schedule.isEmpty()) {
                binding.scheduleSubjectsRecycler.visibility = View.GONE
                binding.scheduleNoLessons.visibility = View.VISIBLE
            } else {
                if (isExams) {
                    binding.scheduleDayHeader.setBackgroundResource(R.drawable.subject_exams_holder)
                }
                val adapter = ScheduleSubjectsAdapter(context, scheduleDay.schedule, isGroupSchedule, showSubjectDialog)
                binding.scheduleSubjectsRecycler.adapter = adapter
                binding.scheduleSubjectsRecycler.layoutManager = LinearLayoutManager(context)
                binding.scheduleWeekDay.text = scheduleDay.weekDayNameUpperFirstLetter()
            }
        }
    }
}


