package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentMainScheduleBinding
import com.bsuir.bsuirschedule.databinding.TabViewsBinding
import com.bsuir.bsuirschedule.domain.models.ScheduleTabs
import com.bsuir.bsuirschedule.presentation.adapters.ScheduleExamsAdapter
import com.bsuir.bsuirschedule.presentation.dialogs.StateDialog
import com.bsuir.bsuirschedule.presentation.popupMenu.MainPopupMenu
import com.bsuir.bsuirschedule.presentation.viewModels.CurrentWeekViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
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

        binding.scheduleViewPager.adapter = ScheduleExamsAdapter(activity!!)
        binding.scheduleItemsTabLayout.visibility = View.VISIBLE

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            TabLayoutMediator(binding.scheduleItemsTabLayout, binding.scheduleViewPager) { tab, position ->
                when (position) {
                    ScheduleTabs.SCHEDULE -> tab.customView = tabViews.schedule
                    ScheduleTabs.EXAMS -> {
                        if (schedule.examsSchedule.isEmpty()) {
                            tabViews.examImage.setBackgroundResource(R.drawable.ic_flag_hint)
                        } else {
                            tabViews.examImage.setBackgroundResource(R.drawable.ic_flag)
                        }
                        tab.customView = tabViews.exams
                    }
                    ScheduleTabs.CALENDAR -> tab.customView = tabViews.calendar
                    ScheduleTabs.CONTROL -> tab.customView = tabViews.scheduleControl
                    else -> tab.customView = tabViews.schedule
                }
            }.attach()
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

        val settingsClick = {
            Navigation.findNavController(binding.root).navigate(R.id.action_mainScheduleFragment_to_settingsFragment)
        }

        val aboutClick = {
            Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.scheduleMoreButton.setOnClickListener {
            val popupMenu = MainPopupMenu(
                context = context!!,
                onSettingsClick = settingsClick,
                onAboutClick = aboutClick
            ).initPopupMenu(binding.scheduleMoreButton)

            popupMenu.show()
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


