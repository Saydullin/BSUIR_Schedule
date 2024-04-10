package com.bsuir.bsuirschedule.presentation.fragments.scheduleSettings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSettingsScheduleBinding
import com.bsuir.bsuirschedule.presentation.viewModels.ScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleSettingsSchedule : Fragment() {

    private lateinit var binding: FragmentScheduleSettingsScheduleBinding
    private val groupScheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)
    private var isUIUpdated: Boolean = true

    override fun onDestroy() {
        super.onDestroy()
        validateChanges()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleSettingsScheduleBinding.inflate(inflater)

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) {schedule ->
            if (schedule == null) return@observe
            val settings = schedule.settings.schedule

            if (isUIUpdated) {
                binding.autoUpdateCheckBox.setChecked(settings.isAutoUpdate)
                binding.emptyDaysCheckBox.setChecked(settings.isShowEmptyDays)
                binding.shortScheduleCheckBox.setChecked(settings.isShowShortSchedule)
                binding.pastDaysAmountEditText.setText(settings.pastDaysNumber.toString())
            }
        }

        return binding.root
    }

    private fun validateChanges() {
        val schedule = groupScheduleVM.getActiveSchedule() ?: return
        val scheduleSettings = schedule.settings.schedule
        val pastDaysText = binding.pastDaysAmountEditText.getText()
        val isShownPastDays = pastDaysText.isNotEmpty() || pastDaysText != "0"

        if (
            pastDaysText.isEmpty() ||
            (pastDaysText.isNotEmpty() && pastDaysText.isDigitsOnly() &&
                    pastDaysText.toInt() != scheduleSettings.pastDaysNumber) ||
            scheduleSettings.isShowPastDays != isShownPastDays ||
            scheduleSettings.isShowEmptyDays != binding.emptyDaysCheckBox.isChecked() ||
            scheduleSettings.isShowShortSchedule != binding.shortScheduleCheckBox.isChecked() ||
            scheduleSettings.isAutoUpdate != binding.autoUpdateCheckBox.isChecked()
        ) {
            updateSettings()
        }
    }

    private fun updateSettings() {
        isUIUpdated = false
        Log.e("sady", "pastDaysNumber: ${binding.pastDaysAmountEditText.getText()}")
        val schedule = groupScheduleVM.getActiveSchedule() ?: return
        val scheduleSettings = schedule.settings
        scheduleSettings.schedule.isShowPastDays = binding.pastDaysAmountEditText.getText().isNotEmpty()
        if (binding.pastDaysAmountEditText.getText().isNotEmpty() && binding.pastDaysAmountEditText.getText().isDigitsOnly()) {
            scheduleSettings.schedule.pastDaysNumber = binding.pastDaysAmountEditText.getText().toInt()
        } else {
            binding.pastDaysAmountEditText.setText("0")
            scheduleSettings.schedule.pastDaysNumber = 0
        }
        binding.pastDaysAmountEditText.clearFocus()
        scheduleSettings.schedule.isShowEmptyDays = binding.emptyDaysCheckBox.isChecked()
        scheduleSettings.schedule.isShowShortSchedule = binding.shortScheduleCheckBox.isChecked()
        scheduleSettings.schedule.isAutoUpdate = binding.autoUpdateCheckBox.isChecked()
        groupScheduleVM.updateScheduleSettings(schedule.id, scheduleSettings)
    }

}


