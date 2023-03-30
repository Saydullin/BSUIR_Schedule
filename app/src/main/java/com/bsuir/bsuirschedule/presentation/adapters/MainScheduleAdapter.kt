package com.bsuir.bsuirschedule.presentation.adapters

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleDayBinding
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.utils.CalendarDate

class MainScheduleAdapter(
    private val context: Context,
    private var data: ArrayList<ScheduleDay>,
    private var isGroupSchedule: Boolean,
    private var showSubjectDialog: ((subject: ScheduleSubject) -> Unit)?,
    private var onLongPress: ((subject: ScheduleSubject, subjectView: View) -> Unit)?
): RecyclerView.Adapter<MainScheduleAdapter.ViewHolder>() {

    private var isShortSchedule = false

    fun updateScheduleData(newData: ArrayList<ScheduleDay>, isGroup: Boolean = false) {
        data = newData
        isGroupSchedule = isGroup
        notifyDataSetChanged()
    }

    fun setShortSchedule(isShort: Boolean) {
        isShortSchedule = isShort
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ScheduleDayBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(isGroupSchedule, isShortSchedule, showSubjectDialog, onLongPress, view)
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
        private val isShortSchedule: Boolean,
        private val showSubjectDialog: ((subject: ScheduleSubject) -> Unit)?,
        private val onLongPress: ((subject: ScheduleSubject, subjectView: View) -> Unit)?,
        private val binding: ScheduleDayBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(scheduleDay: ScheduleDay, context: Context, position: Int) {
            val lessonsText = context.resources.getQuantityString(
                R.plurals.plural_lessons,
                scheduleDay.schedule.size,
                scheduleDay.schedule.size
            )

            when (scheduleDay.date) {
                CalendarDate.YESTERDAY -> {
                    binding.scheduleDate.text = context.getString(R.string.yesterday)
                }
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
                binding.weekNumber.visibility = View.VISIBLE
                binding.weekNumber.text = context.getString(R.string.schedule_week_number, scheduleDay.weekNumber)
            }
            binding.weekNumberDigit.text = scheduleDay.weekNumberString()
            binding.scheduleWeekDay.text = scheduleDay.weekDayNameUpperFirstLetter()
            binding.scheduleLessonsAmount.text = lessonsText
            if (scheduleDay.schedule.isEmpty()) {
                binding.scheduleSubjectsRecycler.visibility = View.GONE
                binding.scheduleNoLessons.visibility = View.VISIBLE
                val typedValue = TypedValue()
                val theme = context.theme
                theme.resolveAttribute(R.attr.textColor, typedValue, true)
                @ColorInt val color = typedValue.data
                binding.scheduleDayHeader.setBackgroundResource(R.drawable.subject_empty_holder)
                binding.scheduleDate.setTextColor(color)
                binding.scheduleWeekDay.setTextColor(color)
                binding.weekNumberDigit.setTextColor(color)
                binding.scheduleLessonsAmount.setTextColor(color)
            } else {
                val adapter = if (isShortSchedule) {
                    ScheduleShortSubjectsAdapter(context, scheduleDay.schedule, isGroupSchedule, showSubjectDialog, onLongPress)
                } else {
                    ScheduleSubjectsAdapter(context, scheduleDay.schedule, isGroupSchedule, showSubjectDialog, onLongPress)
                }
                binding.scheduleSubjectsRecycler.adapter = adapter
                binding.scheduleSubjectsRecycler.layoutManager = LinearLayoutManager(context)
                binding.scheduleWeekDay.text = scheduleDay.weekDayNameUpperFirstLetter()
            }
        }

    }
}


