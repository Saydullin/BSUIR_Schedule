package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSubgroupBinding
import com.bsuir.bsuirschedule.presentation.adapters.SubgroupsAdapter
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleSubgroupFragment : Fragment() {

    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleSubgroupBinding.inflate(inflater)

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule.subgroups.isNotEmpty()) {
                binding.subgroupsRecyclerview.visibility = View.VISIBLE
                binding.placeholder.visibility = View.GONE
                binding.subgroupsRecyclerview.layoutManager = LinearLayoutManager(context)
                binding.subgroupsRecyclerview.adapter = SubgroupsAdapter(schedule.subgroups)
            } else {
                binding.placeholder.visibility = View.VISIBLE
                binding.subgroupsRecyclerview.visibility = View.GONE
            }
        }

        return binding.root
    }

}