package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSubjectAddBinding
import com.bsuir.bsuirschedule.presentation.viewModels.ScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleSubjectAddFragment : Fragment() {

    private val scheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleSubjectAddBinding.inflate(inflater)

        val lessonTypes = listOf(
            getString(R.string.lecture),
            getString(R.string.practise),
            getString(R.string.laboratory),
            getString(R.string.consultation),
            getString(R.string.exam),
        )
        val weekDays = listOf(
            getString(R.string.monday),
            getString(R.string.tuesday),
            getString(R.string.wednesday),
            getString(R.string.thursday),
            getString(R.string.friday),
            getString(R.string.saturday),
            getString(R.string.sunday),
        )

        scheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) return@observe

            binding.addCustomSubjectView.setGroupType(!schedule.isGroup())
            binding.addCustomSubjectView.setLessonTypes(lessonTypes)
            binding.addCustomSubjectView.setWeekDays(weekDays)
            binding.addCustomSubjectView.setSubgroups(schedule.subgroups)
        }

        binding.addCustomSubjectView.setOnSelectScheduleListener {
            // Redirect to select fragment
            // There save selected schedule
            // Then setGroup or setEmployee
        }

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectAddFragment_to_mainScheduleFragment)
        }

        return binding.root
    }
}