package com.example.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bsuirschedule.R
import com.example.bsuirschedule.databinding.ScheduleListItemBinding
import com.example.bsuirschedule.domain.models.SavedSchedule

class SavedItemsAdapter(
    private val context: Context,
    private val savedSchedules: ArrayList<SavedSchedule>,
    private val saveScheduleLambda: (schedule: SavedSchedule) -> Unit,
    private val longPressLambda: (schedule: SavedSchedule, view: View) -> Unit,
) : RecyclerView.Adapter<SavedItemsAdapter.ViewHolder>() {

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
                    .into(binding.nestedGroup.image)
                binding.nestedGroup.title.text = group.name
                binding.nestedGroup.course.text = "${group.course} $courseText"
                binding.nestedGroup.departments.text = group.getFacultyAndSpecialityAbbr()
                binding.nestedGroup.educationType.text = group.speciality?.educationForm?.name ?: ""
            } else {
                val employee = schedule.employee
                Glide.with(context)
                    .load(employee.photoLink)
                    .placeholder(R.drawable.ic_person_placeholder)
                    .into(binding.nestedGroup.image)
                binding.nestedGroup.title.text = employee.getFullName()
                binding.nestedGroup.course.visibility = View.GONE
                binding.nestedGroup.educationType.text = employee.getShortDepartmentsAbbr()
                binding.nestedGroup.departments.text = employee.getDegreeAndRank()
            }
//            if (schedule.lastUpdate != "") {
//                binding.lastUpdate.visibility = View.VISIBLE
//                binding.lastUpdate.text = schedule.lastUpdate
//            }

            binding.root.setOnClickListener {
                saveScheduleLambda(schedule)
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


