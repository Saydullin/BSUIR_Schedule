package com.bsuir.bsuirschedule.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bsuir.bsuirschedule.domain.models.ScheduleTabs
import com.bsuir.bsuirschedule.presentation.fragments.*

class ScheduleExamsAdapter(
    fa: FragmentActivity,
): FragmentStateAdapter(fa) {

    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            ScheduleTabs.SCHEDULE -> ScheduleRecyclerFragment()
            ScheduleTabs.EXAMS -> ExamsRecyclerFragment()
            ScheduleTabs.CALENDAR -> ScheduleCalendarFragment()
            ScheduleTabs.CONTROL -> ScheduleControlFragment()
            else -> ScheduleRecyclerFragment()
        }
    }

}


