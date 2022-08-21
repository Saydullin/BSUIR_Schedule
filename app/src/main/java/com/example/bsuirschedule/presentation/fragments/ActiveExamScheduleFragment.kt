package com.example.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bsuirschedule.databinding.FragmentActiveExamScheduleBinding
import com.example.bsuirschedule.databinding.TabViewsBinding
import com.example.bsuirschedule.presentation.adapters.ScheduleExamsAdapter
import com.example.bsuirschedule.presentation.adapters.ScheduleItemsVPAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ActiveExamScheduleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentActiveExamScheduleBinding.inflate(inflater)
        val tabViews = TabViewsBinding.inflate(inflater)

        binding.scheduleViewPager.adapter = ScheduleExamsAdapter(activity!!)

        TabLayoutMediator(binding.scheduleItemsTabLayout, binding.scheduleViewPager) { tab, position ->
            when (position) {
                0 -> tab.customView = tabViews.group
                1 -> tab.customView = tabViews.employee
                else -> tab.customView = tabViews.group
            }
        }.attach()

        return binding.root
    }

}