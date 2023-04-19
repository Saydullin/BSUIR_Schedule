package com.bsuir.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleUpdatedDayBinding
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleUpdatedDay

class ScheduleUpdatedDayAdapter(
    private val context: Context,
    private var data: ArrayList<ScheduleUpdatedDay>,
    private var isGroupSchedule: Boolean,
    private var showActionDialog: (action: ScheduleSubject) -> Unit,
): RecyclerView.Adapter<ScheduleUpdatedDayAdapter.ViewHolder>() {

    fun updateScheduleData(newData: ArrayList<ScheduleUpdatedDay>, isGroup: Boolean = false) {
        data = newData
        isGroupSchedule = isGroup
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ScheduleUpdatedDayBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(isGroupSchedule, showActionDialog, view)
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
        private val showActionDialog: (action: ScheduleSubject) -> Unit,
        private val binding: ScheduleUpdatedDayBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(scheduleUpdatedDay: ScheduleUpdatedDay, context: Context) {
            val changesCountText = context.resources.getQuantityString(
                R.plurals.plural_changes,
                scheduleUpdatedDay.actions.size,
                scheduleUpdatedDay.actions.size
            )
            binding.scheduleDate.text = scheduleUpdatedDay.getDate(context)

            binding.scheduleWeekDay.text = scheduleUpdatedDay.getDayOfWeek()
            binding.scheduleLessonsAmount.text = changesCountText

            val adapter = ScheduleUpdatedActionAdapter(
                context,
                scheduleUpdatedDay.actions,
                isGroupSchedule,
                showActionDialog
            )
            binding.scheduleSubjectsRecycler.adapter = adapter
            binding.scheduleSubjectsRecycler.layoutManager = LinearLayoutManager(context)
        }

    }
}


