package com.example.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.bsuirschedule.R
import com.example.bsuirschedule.databinding.FragmentMainScheduleBinding
import com.example.bsuirschedule.databinding.TabViewsBinding
import com.example.bsuirschedule.presentation.adapters.ScheduleExamsAdapter
import com.example.bsuirschedule.presentation.dialogs.StateDialog
import com.example.bsuirschedule.presentation.viewModels.CurrentWeekViewModel
import com.example.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.navigation.koinNavGraphViewModel

class MainScheduleFragment : Fragment() {

    private val currentWeekVM: CurrentWeekViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)
    private lateinit var binding: FragmentMainScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScheduleBinding.inflate(inflater)
        val tabViews = TabViewsBinding.inflate(inflater)
        val weekText = getString(R.string.week)

        groupScheduleVM.examsScheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule != null) {
                binding.scheduleViewPager.adapter = ScheduleExamsAdapter(activity!!, true)
                binding.scheduleItemsTabLayout.visibility = View.VISIBLE

                TabLayoutMediator(binding.scheduleItemsTabLayout, binding.scheduleViewPager) { tab, position ->
                    when (position) {
                        0 -> tab.customView = tabViews.schedule
                        1 -> tab.customView = tabViews.exams
                        else -> tab.customView = tabViews.schedule
                    }
                }.attach()
            } else {
                binding.scheduleViewPager.adapter = ScheduleExamsAdapter(activity!!)
                binding.scheduleItemsTabLayout.visibility = View.GONE
            }
        }

        currentWeekVM.getCurrentWeek()
        currentWeekVM.currentWeekStatus.observe(viewLifecycleOwner) { currentWeek ->
            binding.titleWeekNumber.text = "$currentWeek $weekText"
        }

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule.id == -1) {
                binding.hiddenPlaceholder.visibility = View.VISIBLE
                binding.mainScheduleContent.visibility = View.GONE
            } else {
                binding.hiddenPlaceholder.visibility = View.GONE
                binding.mainScheduleContent.visibility = View.VISIBLE
            }
        }

        binding.scheduleListButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_to_schedules_list)
        }

        currentWeekVM.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (errorStatus != null) {
                val stateStatus = StateDialog(errorStatus)
                stateStatus.isCancelable = true
                stateStatus.show(parentFragmentManager, "ErrorDialog")
                currentWeekVM.closeError()
            }
        }



        return binding.root
    }

}


