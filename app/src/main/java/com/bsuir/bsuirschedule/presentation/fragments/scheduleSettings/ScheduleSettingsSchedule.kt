package com.bsuir.bsuirschedule.presentation.fragments.scheduleSettings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSettingsScheduleBinding
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleSettingsSchedule : Fragment() {

    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleSettingsScheduleBinding.inflate(inflater)

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) return@observe
            val settings = schedule.settings.schedule

            binding.autoUpdateCheckBox.isChecked = settings.isAutoUpdate
            binding.emptyDaysCheckBox.isChecked = settings.isShowEmptyDays
            binding.pastDaysCheckBox.isChecked = settings.isShowPastDays
            binding.pastDaysAmountEditText.setText(settings.pastDaysNumber.toString())
            binding.pastDaysAmountEditText.setSelection(binding.pastDaysAmountEditText.length())
        }

        binding.resetButton.setOnClickListener {
            val schedule = groupScheduleVM.scheduleStatus.value ?: return@setOnClickListener
            val scheduleSettings = schedule.settings
            scheduleSettings.schedule = ScheduleSettings.empty.schedule
            groupScheduleVM.updateScheduleSettings(schedule.id, scheduleSettings)
        }

        binding.saveButton.setOnClickListener {
            val schedule = groupScheduleVM.scheduleStatus.value ?: return@setOnClickListener
            val scheduleSettings = schedule.settings
            scheduleSettings.schedule.isShowPastDays = binding.pastDaysCheckBox.isChecked
            if (binding.pastDaysAmountEditText.text.isDigitsOnly()) {
                scheduleSettings.schedule.pastDaysNumber = binding.pastDaysAmountEditText.text.toString().toInt()
            } else {
                scheduleSettings.schedule.pastDaysNumber = 0
            }
            scheduleSettings.schedule.isShowEmptyDays = binding.emptyDaysCheckBox.isChecked
            scheduleSettings.schedule.isAutoUpdate = binding.autoUpdateCheckBox.isChecked
            groupScheduleVM.updateScheduleSettings(schedule.id, scheduleSettings)
        }

        return binding.root
    }
}


