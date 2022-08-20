package com.example.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.bsuirschedule.R
import com.example.bsuirschedule.databinding.FragmentScheduleItemsBinding
import com.example.bsuirschedule.presentation.viewModels.EmployeeItemsViewModel
import com.example.bsuirschedule.presentation.viewModels.GroupItemsViewModel
import com.example.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import com.example.bsuirschedule.presentation.viewModels.SavedSchedulesViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

// Full list of group items
class ScheduleItemsFragment : Fragment() {

    private val savedScheduleVM: SavedSchedulesViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupSchedule: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)
    private val employeeItemsVM: EmployeeItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupItemsVM: GroupItemsViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleItemsBinding.inflate(inflater)

        groupItemsVM.getAllGroupItems()
        employeeItemsVM.getAllEmployeeItems()

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_to_saved_schedules)
        }

        groupSchedule.activeScheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule != null) {
                savedScheduleVM.saveSchedule(schedule)
            }
        }

        return binding.root
    }

}


