package com.bsuir.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.GroupItemBinding
import com.bsuir.bsuirschedule.domain.models.Group

class GroupItemsAdapter(
    val context: Context,
    val data: ArrayList<Group>,
    private val saveGroupLambda: (group: Group) -> Unit
): RecyclerView.Adapter<GroupItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = GroupItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(view, saveGroupLambda)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = data[position]

        holder.bind(context, group)
    }

    override fun getItemCount() = data.size

    class ViewHolder(groupItemBinding: GroupItemBinding, private val saveGroupLambda: (group: Group) -> Unit): RecyclerView.ViewHolder(groupItemBinding.root) {
        private val binding = groupItemBinding

        fun bind(context: Context, group: Group) {
            val courseString = context.resources.getString(R.string.course)

            binding.nestedGroup.title.text = group.name
            binding.nestedGroup.course.text = "${group.course} $courseString"
            binding.nestedGroup.departments.text = group.getFacultyAndSpecialityAbbr()
            binding.nestedGroup.educationType.text = group.speciality?.educationForm?.name ?: ""

            binding.root.setOnClickListener {
                saveGroupLambda(group)
            }
        }

    }

}