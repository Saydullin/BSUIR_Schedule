package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            val subgroups = ArrayList<String>()
            subgroups.add(resources.getString(R.string.settings_all_subgroups))
            schedule.subgroups.forEach { subgroup ->
                if (subgroup != 0) {
                    subgroups.add(resources.getString(R.string.settings_item_subgroup, subgroup))
                }
            }
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, subgroups)
            binding.scheduleSubgroupSettings.autoCompleteTextView.setAdapter(arrayAdapter)
        }

        return binding.root
    }

}


