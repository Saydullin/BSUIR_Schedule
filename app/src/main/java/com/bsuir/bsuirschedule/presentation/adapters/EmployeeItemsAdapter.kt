package com.bsuir.bsuirschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.EmployeeItemBinding
import com.bsuir.bsuirschedule.domain.models.Employee

class EmployeeItemsAdapter(
    val context: Context,
    val data: ArrayList<Employee>,
    private var saveEmployeeCallback: ((employee: Employee) -> Unit)?
): RecyclerView.Adapter<EmployeeItemsAdapter.ViewHolder>() {

    fun setEmployeeList(newEmployeeList: ArrayList<Employee>) {
        data.clear()
        data.addAll(newEmployeeList)
        notifyDataSetChanged()
    }

    fun setSavedItem(item: Employee) {
        val position = data.indexOfFirst { it.id == item.id }
        if (position != -1) {
            data[position] = item
            notifyItemChanged(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = EmployeeItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(view, saveEmployeeCallback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employeeItem = data[position]

        holder.bind(context, employeeItem)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun getItemCount() = data.size

    class ViewHolder(employeeItemBinding: EmployeeItemBinding, private val saveEmployeeLambda: ((group: Employee) -> Unit)?): RecyclerView.ViewHolder(employeeItemBinding.root) {
        private val binding = employeeItemBinding

        fun bind(context: Context, employee: Employee) {
            binding.nestedEmployee.title.text = employee.getTitleOrFullName()
            binding.nestedEmployee.departments.text = employee.getDegreeAndRank()
            binding.nestedEmployee.educationType.text = employee.getShortDepartmentsAbbr()

            if (employee.isSaved) {
                binding.nestedEmployee.iconAdded.visibility = View.VISIBLE
            } else {
                binding.nestedEmployee.iconAdded.visibility = View.GONE
            }

            Glide.with(context)
                .load(employee.photoLink)
                .placeholder(R.drawable.ic_person_placeholder)
                .into(binding.nestedEmployee.image)

            binding.root.setOnClickListener {
                saveEmployeeLambda?.let { it1 -> it1(employee) }
            }
        }

    }

}


