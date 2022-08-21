package com.example.bsuirschedule.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bsuirschedule.presentation.fragments.AllEmployeeItemsFragment
import com.example.bsuirschedule.presentation.fragments.ExamsRecyclerFragment
import com.example.bsuirschedule.presentation.fragments.ScheduleRecyclerFragment

class ScheduleExamsAdapter(
    fa: FragmentActivity,
    private val isHasExams: Boolean = false
): FragmentStateAdapter(fa) {

    override fun getItemCount() = if (isHasExams) 2 else 1

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ScheduleRecyclerFragment()
            1 -> ExamsRecyclerFragment()
            else -> ScheduleRecyclerFragment()
        }
    }

}