package com.bsuir.bsuirschedule.presentation.fragments.scheduleSettings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSettingsExamsBinding
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleSettingsExams : Fragment() {

    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleSettingsExamsBinding.inflate(inflater)

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) return@observe
            val settings = schedule.settings.exams

            binding.emptyDaysCheckBox.isChecked = settings.isShowEmptyDays
            binding.pastDaysCheckBox.isChecked = settings.isShowPastDays
        }

        binding.emptyDaysCheckBox.setOnCheckedChangeListener { _, _ ->
            validateChanges(binding)
        }

        binding.pastDaysCheckBox.setOnCheckedChangeListener { _, _ ->
            validateChanges(binding)
        }

        binding.resetButton.setOnClickListener {
            val schedule = groupScheduleVM.getActiveSchedule() ?: return@setOnClickListener
            val scheduleSettings = schedule.settings
            scheduleSettings.exams = ScheduleSettings.empty.exams
            groupScheduleVM.updateScheduleSettings(schedule.id, scheduleSettings)
        }

        binding.saveButton.setOnClickListener {
            val schedule = groupScheduleVM.getActiveSchedule() ?: return@setOnClickListener
            val scheduleSettings = schedule.settings
            binding.saveButton.animate().setDuration(300).alpha(0f)
            binding.saveButton.isEnabled = false
            scheduleSettings.exams.isShowPastDays = binding.pastDaysCheckBox.isChecked
            scheduleSettings.exams.isShowEmptyDays = binding.emptyDaysCheckBox.isChecked
            groupScheduleVM.updateScheduleSettings(schedule.id, scheduleSettings)
        }

        return binding.root
    }

    private fun validateChanges(binding: FragmentScheduleSettingsExamsBinding) {
        val schedule = groupScheduleVM.getActiveSchedule() ?: return
        val scheduleSettings = schedule.settings.exams

        if (
            scheduleSettings.isShowEmptyDays != binding.emptyDaysCheckBox.isChecked ||
            scheduleSettings.isShowPastDays != binding.pastDaysCheckBox.isChecked
        ) {
            binding.saveButton.animate().setDuration(300).alpha(1f)
            binding.saveButton.isEnabled = true
        } else {
            binding.saveButton.animate().setDuration(300).alpha(0f)
            binding.saveButton.isEnabled = false
        }

    }

}