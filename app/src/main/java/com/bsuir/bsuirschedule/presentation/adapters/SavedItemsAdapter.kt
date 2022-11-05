package com.bsuir.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleListItemBinding
import com.bsuir.bsuirschedule.domain.models.SavedSchedule

class SavedItemsAdapter(
    private val context: Context,
    private val savedSchedules: ArrayList<SavedSchedule>,
    private val saveScheduleLambda: (schedule: SavedSchedule) -> Unit,
    private val longPressLambda: (schedule: SavedSchedule, view: View) -> Unit,
) : RecyclerView.Adapter<SavedItemsAdapter.ViewHolder>() {

    fun updateItems(newItems: ArrayList<SavedSchedule>) {
        savedSchedules.clear()
        savedSchedules.addAll(newItems)
        notifyDataSetChanged()
    }

    fun updateItem(savedSchedule: SavedSchedule) {
        val position = savedSchedules.indexOfFirst { it.id == savedSchedule.id && (it.isGroup == savedSchedule.isGroup)}
        if (position != -1) {
            savedSchedules[position] = savedSchedule
            notifyItemChanged(position)
        }
    }

    fun removeItem(savedSchedule: SavedSchedule) {
        val position = savedSchedules.indexOfFirst { it.id == savedSchedule.id && (it.isGroup == savedSchedule.isGroup)}
        if (position != -1) {
            savedSchedules.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    class ViewHolder(
        scheduleListItemBinding: ScheduleListItemBinding,
        private val saveScheduleLambda: (schedule: SavedSchedule) -> Unit,
        private val longPressLambda: (schedule: SavedSchedule, view: View) -> Unit,
    ) : RecyclerView.ViewHolder(scheduleListItemBinding.root) {
        private val binding = scheduleListItemBinding

        fun bind(context: Context, schedule: SavedSchedule) {
            val courseText = context.getString(R.string.course)
            if (schedule.isGroup) {
                val group = schedule.group
                Glide.with(context)
                    .load(R.drawable.ic_group_placeholder)
                    .into(binding.image)
                binding.title.text = group.getTitleOrName()
                binding.course.text = "${group.course} $courseText"
                binding.departments.text = group.getFacultyAndSpecialityAbbr()
                binding.educationType.text = group.speciality?.educationForm?.name ?: ""
            } else {
                val employee = schedule.employee
                Glide.with(context)
                    .load(employee.photoLink)
                    .placeholder(R.drawable.ic_person_placeholder)
                    .into(binding.image)
                binding.title.text = employee.getTitleOrFullName()
                binding.course.visibility = View.GONE
                binding.educationType.text = employee.getShortDepartmentsAbbr()
                binding.departments.text = employee.getDegreeAndRank()
            }

            if (schedule.isExistExams) {
                binding.iconFlag.visibility = View.VISIBLE
            } else {
                binding.iconFlag.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                saveScheduleLambda(schedule)
            }

            binding.moreButton.setOnClickListener {
                longPressLambda(schedule, binding.moreButton)
            }

            binding.root.setOnLongClickListener {
                longPressLambda(schedule, binding.root)
                true
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ScheduleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view, saveScheduleLambda, longPressLambda)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scheduleItem = savedSchedules[position]

        holder.bind(context, scheduleItem)
    }

    override fun getItemCount() = savedSchedules.size

}


