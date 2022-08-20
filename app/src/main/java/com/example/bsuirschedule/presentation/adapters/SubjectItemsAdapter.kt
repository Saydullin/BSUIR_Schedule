package com.example.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bsuirschedule.R
import com.example.bsuirschedule.databinding.SubjectItemSourceBinding
import com.example.bsuirschedule.domain.models.SavedSchedule

class SubjectItemsAdapter(
    private val context: Context,
    private val items: ArrayList<SavedSchedule>
): RecyclerView.Adapter<SubjectItemsAdapter.ViewHolder>() {

    class ViewHolder(private val binding: SubjectItemSourceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, item: SavedSchedule) {
            val courseText = context.getString(R.string.course)
            if (item.isGroup) {
                val group = item.group
                Glide.with(context)
                    .load(R.drawable.ic_group_placeholder)
                    .into(binding.nestedGroup.image)
                binding.nestedGroup.course.text = "${group.course} $courseText"
                binding.nestedGroup.title.text = group.name
                binding.nestedGroup.departments.text = group.getFacultyAndSpecialityAbbr()
                binding.nestedGroup.educationType.text = group.speciality?.educationForm?.name ?: ""
            } else {
                val employee = item.employee
                Glide.with(context)
                    .load(employee.photoLink)
                    .into(binding.nestedGroup.image)
                binding.nestedGroup.course.visibility = View.GONE
                binding.nestedGroup.educationType.text = employee.getShortDepartmentsAbbr()
                binding.nestedGroup.title.text = employee.getFullName()
                binding.nestedGroup.departments.text = employee.getDegreeAndRank()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SubjectItemSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.bind(context, item)
    }

    override fun getItemCount() = items.size

}