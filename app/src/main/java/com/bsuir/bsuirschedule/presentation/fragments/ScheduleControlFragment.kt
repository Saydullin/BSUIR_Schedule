package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleControlBinding
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleControlFragment : Fragment() {

    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleControlBinding.inflate(inflater)
        val subgroupBinding = binding.scheduleSubgroupSettings

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            val subgroups = ArrayList<String>()
            val allSubgroupsText = resources.getString(R.string.settings_all_subgroups)
            schedule.subgroups.forEach { subgroup ->
                if (subgroup == 0) {
                    subgroups.add(allSubgroupsText)
                } else {
                    subgroups.add(resources.getString(R.string.settings_item_subgroup, subgroup))
                }
            }
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, subgroups)
            subgroupBinding.autoCompleteTextView.setAdapter(arrayAdapter)
            val selectedSubgroup = schedule.selectedSubgroup
            val selectionText = if (selectedSubgroup == 0) {
                allSubgroupsText
            } else {
                resources.getString(R.string.settings_item_subgroup, selectedSubgroup)
            }
            subgroupBinding.autoCompleteTextView.setText(selectionText, false)
        }

        subgroupBinding.autoCompleteTextView.setOnItemClickListener { _, _, i, _ ->
            val schedule = groupScheduleVM.scheduleStatus.value
            if (schedule != null && schedule.selectedSubgroup != schedule.subgroups[i]) {
                schedule.selectedSubgroup = schedule.subgroups[i]
                groupScheduleVM.changeSubgroup(schedule)
                Toast.makeText(context, resources.getString(R.string.subgroup_selected, schedule.subgroups[i]), Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

}


