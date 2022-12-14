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
import com.bsuir.bsuirschedule.presentation.adapters.ScheduleVPAdapter
import com.bsuir.bsuirschedule.presentation.dialogs.StateDialog
import com.bsuir.bsuirschedule.presentation.popupMenu.MainPopupMenu
import com.bsuir.bsuirschedule.presentation.utils.ErrorMessage
import com.bsuir.bsuirschedule.presentation.viewModels.*
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.navigation.koinNavGraphViewModel

class MainScheduleFragment : Fragment() {

    private val employeeItemsVM: EmployeeItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupItemsVM: GroupItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val savedItemsVM: SavedSchedulesViewModel by koinNavGraphViewModel(R.id.navigation)
    private val currentWeekVM: CurrentWeekViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)
    private lateinit var binding: FragmentMainScheduleBinding

    override fun onResume() {
        super.onResume()

        groupScheduleVM.updateSchedule()
    }

    override fun onPause() {
        super.onPause()

        groupScheduleVM.setUpdateStatus(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScheduleBinding.inflate(inflater)
        val tabViews = TabViewsBinding.inflate(inflater)
        val weekText = getString(R.string.week)

        binding.scheduleViewPager.adapter = ScheduleVPAdapter(activity!!)
        binding.scheduleItemsTabLayout.visibility = View.VISIBLE

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) {
                binding.hiddenPlaceholder.visibility = View.VISIBLE
                binding.mainScheduleContent.visibility = View.GONE
                return@observe
            }
            binding.hiddenPlaceholder.visibility = View.GONE
            binding.mainScheduleContent.visibility = View.VISIBLE

            TabLayoutMediator(binding.scheduleItemsTabLayout, binding.scheduleViewPager) { tab, position ->
                when (position) {
                    ScheduleTabs.SCHEDULE -> tab.customView = tabViews.schedule
                    ScheduleTabs.EXAMS -> tab.customView = tabViews.exams
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

        groupScheduleVM.successStatus.observe(viewLifecycleOwner) { successCode ->
            if (successCode != null) {
                val messageManager = ErrorMessage(context!!).get(successCode)
                groupScheduleVM.setSuccessNull()
                Toast.makeText(context, messageManager.title, Toast.LENGTH_SHORT).show()
            }
        }

        groupScheduleVM.scheduleLoadedStatus.observe(viewLifecycleOwner) { savedSchedule ->
            if (savedSchedule == null) return@observe
            if (savedSchedule.isGroup) {
                savedSchedule.group.isSaved = true
                groupItemsVM.saveGroupItem(savedSchedule.group)
            } else {
                savedSchedule.employee.isSaved = true
                employeeItemsVM.saveEmployeeItem(savedSchedule.employee)
            }
            savedItemsVM.saveSchedule(savedSchedule)
            groupScheduleVM.setScheduleLoadedNull()
        }

        return binding.root
    }

}


