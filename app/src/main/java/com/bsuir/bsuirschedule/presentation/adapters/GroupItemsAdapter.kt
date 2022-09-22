package com.bsuir.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.GroupItemBinding
import com.bsuir.bsuirschedule.domain.models.Group
import com.bsuir.bsuirschedule.domain.models.SavedSchedule

class GroupItemsAdapter(
    val context: Context,
    val data: ArrayList<Group>,
    private val savedData: ArrayList<SavedSchedule>,
    private val saveGroupLambda: (group: Group) -> Unit
): RecyclerView.Adapter<GroupItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = GroupItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(view, saveGroupLambda)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = data[position]
        val isSaved = savedData.find { it.id == group.id }

        holder.bind(context, group, isSaved != null)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun getItemCount() = data.size

    class ViewHolder(
        groupItemBinding: GroupItemBinding,
        private val saveGroupLambda: (group: Group) -> Unit
    ): RecyclerView.ViewHolder(groupItemBinding.root) {
        private val binding = groupItemBinding

        fun bind(context: Context, group: Group, isSaved: Boolean) {
            val courseString = context.resources.getString(R.string.course)

            if (isSaved) {
                binding.nestedGroup.iconAdded.visibility = View.VISIBLE
            }
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