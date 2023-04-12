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
import com.bsuir.bsuirschedule.domain.models.ScheduleDayUpdateHistory
import com.bsuir.bsuirschedule.domain.models.ScheduleSubjectHistory
import com.bsuir.bsuirschedule.domain.utils.CalendarDate

class ScheduleDaysUpdateHistoryAdapter(
    private val context: Context,
): RecyclerView.Adapter<ScheduleDaysUpdateHistoryAdapter.ViewHolder>() {

    private var data = ArrayList<ScheduleDayUpdateHistory>()
    private var isGroupSchedule = false
    private var showSubjectDialog: ((subject: ScheduleSubjectHistory) -> Unit)? = null

    fun updateSchedule(
        newData: ArrayList<ScheduleDayUpdateHistory>,
        isGroup: Boolean,
        subjectDialog: ((subject: ScheduleSubjectHistory) -> Unit)?,
    ) {
        isGroupSchedule = isGroup
        data.clear()
        data.addAll(newData)
        showSubjectDialog = subjectDialog
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ScheduleDayBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(isGroupSchedule, showSubjectDialog, view)
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
        private val showSubjectDialog: ((subject: ScheduleSubjectHistory) -> Unit)?,
        private val binding: ScheduleDayBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(scheduleDayHistory: ScheduleDayUpdateHistory, context: Context, position: Int) {
            val scheduleDay = scheduleDayHistory.scheduleDay
            val lessonsText = context.resources.getQuantityString(
                R.plurals.plural_lessons,
                scheduleDayHistory.scheduleActions.size,
                scheduleDayHistory.scheduleActions.size
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
                val adapter = ScheduleUpdateHistorySubjectsAdapter(
                    context,
//                    scheduleDayHistory.scheduleActions,
                    ArrayList(),
                    isGroupSchedule,
                    showSubjectDialog,
                )
                binding.scheduleSubjectsRecycler.adapter = adapter
                binding.scheduleSubjectsRecycler.layoutManager = LinearLayoutManager(context)
                binding.scheduleWeekDay.text = scheduleDay.weekDayNameUpperFirstLetter()
            }
        }
    }
}


