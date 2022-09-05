package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bsuir.bsuirschedule.databinding.FragmentScheduleItemTabsBinding
import com.bsuir.bsuirschedule.databinding.TabViewsBinding
import com.bsuir.bsuirschedule.presentation.adapters.ScheduleItemsVPAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ScheduleItemTabs : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleItemTabsBinding.inflate(inflater)
        val tabViews = TabViewsBinding.inflate(inflater)

        binding.scheduleItemsViewPager.adapter = ScheduleItemsVPAdapter(activity!!)

        TabLayoutMediator(binding.scheduleItemsTabLayout, binding.scheduleItemsViewPager) { tab, position ->
            when (position) {
                0 -> tab.customView = tabViews.group
                1 -> tab.customView = tabViews.employee
                else -> tab.customView = tabViews.group
            }
        }.attach()

        return binding.root
    }

}