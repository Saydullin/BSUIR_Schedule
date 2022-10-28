package com.bsuir.bsuirschedule.presentation.fragments.scheduleSettings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSettingsSubgroupBinding
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleSettingsSubgroup : Fragment() {

    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleSettingsSubgroupBinding.inflate(inflater)
        val allSubgroupsText = resources.getString(R.string.settings_all_subgroups)

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) return@observe
            val subgroups = ArrayList<String>()
            schedule.subgroups.forEach { subgroup ->
                if (subgroup == 0) {
                    subgroups.add(allSubgroupsText)
                } else {
                    subgroups.add(resources.getString(R.string.settings_item_subgroup, subgroup))
                }
            }
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, subgroups)
            binding.autoCompleteTextView.setAdapter(arrayAdapter)
            val selectedSubgroup = schedule.settings.subgroup.selectedNum
            val selectionText = if (selectedSubgroup == 0) {
                allSubgroupsText
            } else {
                resources.getString(R.string.settings_item_subgroup, selectedSubgroup)
            }
            binding.autoCompleteTextView.setText(selectionText, false)
        }

        binding.autoCompleteTextView.setOnItemClickListener { _, _, i, _ ->
            val schedule = groupScheduleVM.getActiveSchedule() ?: return@setOnItemClickListener
            val scheduleSettings = schedule.settings
            if (scheduleSettings.subgroup.selectedNum != schedule.subgroups[i]) {
                scheduleSettings.subgroup.selectedNum = schedule.subgroups[i]
                groupScheduleVM.updateScheduleSettings(schedule.id, scheduleSettings)
                if (schedule.subgroups[i] == 0) {
                    Toast.makeText(context, allSubgroupsText, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, resources.getString(R.string.subgroup_selected, schedule.subgroups[i]), Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

}