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
import com.bsuir.bsuirschedule.domain.models.SavedSchedule

class EmployeeItemsAdapter(
    val context: Context,
    val data: ArrayList<Employee>,
    val savedData: ArrayList<SavedSchedule>,
    private val saveEmployeeLambda: (employee: Employee) -> Unit
): RecyclerView.Adapter<EmployeeItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = EmployeeItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(view, saveEmployeeLambda)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employeeItem = data[position]
        val isAdded = savedData.find { it.id == employeeItem.id }

        holder.bind(context, employeeItem, isAdded != null)
    }

    override fun getItemCount() = data.size

    class ViewHolder(employeeItemBinding: EmployeeItemBinding, private val saveEmployeeLambda: (group: Employee) -> Unit): RecyclerView.ViewHolder(employeeItemBinding.root) {
        private val binding = employeeItemBinding

        fun bind(context: Context, employee: Employee, isAdded: Boolean) {
            binding.nestedEmployee.title.text = employee.getFullName()
            binding.nestedEmployee.departments.text = employee.getDegreeAndRank()
            binding.nestedEmployee.educationType.text = employee.getShortDepartmentsAbbr()

            if (isAdded) {
                binding.nestedEmployee.iconAdded.visibility = View.VISIBLE
            }

            Glide.with(context)
                .load(employee.photoLink)
                .placeholder(R.drawable.ic_person_placeholder)
                .into(binding.nestedEmployee.image)

            binding.root.setOnClickListener {
                saveEmployeeLambda(employee)
            }
        }

    }

}


