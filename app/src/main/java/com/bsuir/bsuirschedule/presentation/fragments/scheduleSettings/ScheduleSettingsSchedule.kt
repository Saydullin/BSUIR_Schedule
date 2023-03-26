package com.bsuir.bsuirschedule.presentation.fragments.scheduleSettings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSettingsScheduleBinding
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.presentation.viewModels.ScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleSettingsSchedule : Fragment() {

    private lateinit var binding: FragmentScheduleSettingsScheduleBinding
    private val groupScheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleSettingsScheduleBinding.inflate(inflater)

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) {schedule ->
            if (schedule == null) return@observe
            val settings = schedule.settings.schedule

            binding.autoUpdateCheckBox.setChecked(settings.isAutoUpdate)
            binding.emptyDaysCheckBox.setChecked(settings.isShowEmptyDays)
            binding.pastDaysCheckBox.setChecked(settings.isShowPastDays)
            checkPastDays(settings.isShowPastDays)
            binding.shortScheduleCheckBox.setChecked(settings.isShowShortSchedule)
            binding.pastDaysAmountEditText.setText(settings.pastDaysNumber.toString())
            binding.pastDaysAmountEditText.setSelection(binding.pastDaysAmountEditText.length())
        }

        binding.autoUpdateCheckBox.setOnCheckListener {
            validateChanges(binding)
        }

        binding.emptyDaysCheckBox.setOnCheckListener {
            validateChanges(binding)
        }

        binding.shortScheduleCheckBox.setOnCheckListener {
            validateChanges(binding)
        }

        binding.resetButton.setOnClickListener {
            val schedule = groupScheduleVM.getActiveSchedule() ?: return@setOnClickListener
            val scheduleSettings = schedule.settings
            scheduleSettings.schedule = ScheduleSettings.empty.schedule
            groupScheduleVM.updateScheduleSettings(schedule.id, scheduleSettings)
        }

        binding.pastDaysAmountEditText.setTextChangeListener {
            validateChanges(binding)
        }

        binding.pastDaysCheckBox.setOnCheckListener { isChecked: Boolean ->
            checkPastDays(isChecked)
            validateChanges(binding)
        }

        binding.saveButton.setOnClickListener {
            val schedule = groupScheduleVM.getActiveSchedule() ?: return@setOnClickListener
            val scheduleSettings = schedule.settings
            binding.saveButton.animate().setDuration(300).alpha(0f)
            binding.saveButton.isEnabled = false
            scheduleSettings.schedule.isShowPastDays = binding.pastDaysCheckBox.isChecked()
            if (binding.pastDaysAmountEditText.getText().isNotEmpty() && binding.pastDaysAmountEditText.getText().isDigitsOnly()) {
                scheduleSettings.schedule.pastDaysNumber = binding.pastDaysAmountEditText.getText().toInt()
            } else {
                scheduleSettings.schedule.pastDaysNumber = 0
            }
            binding.pastDaysAmountEditText.clearFocus()
            scheduleSettings.schedule.isShowEmptyDays = binding.emptyDaysCheckBox.isChecked()
            scheduleSettings.schedule.isShowShortSchedule = binding.shortScheduleCheckBox.isChecked()
            scheduleSettings.schedule.isAutoUpdate = binding.autoUpdateCheckBox.isChecked()
            groupScheduleVM.updateScheduleSettings(schedule.id, scheduleSettings)
        }

        return binding.root
    }

    fun checkPastDays(isChecked: Boolean) {
        val containerHeight = binding.pastDaysCheckBox.height
        if (isChecked) {
            binding.pastDaysShowAmount.visibility = View.VISIBLE
            binding.pastDaysShowAmount.animate().alpha(1f).translationY(0f).duration = 200L
        } else {
            binding.pastDaysShowAmount.visibility = View.GONE
            binding.pastDaysShowAmount.translationY = (-containerHeight).toFloat()
            binding.pastDaysAmountEditText.clearInputFocus()
        }
    }

    private fun validateChanges(binding: FragmentScheduleSettingsScheduleBinding) {
        val schedule = groupScheduleVM.getActiveSchedule() ?: return
        val scheduleSettings = schedule.settings.schedule

        if (
            (binding.pastDaysAmountEditText.getText().isNotEmpty() && binding.pastDaysAmountEditText.getText().isDigitsOnly() &&
            binding.pastDaysAmountEditText.getText().toInt() != scheduleSettings.pastDaysNumber) ||
            scheduleSettings.isShowPastDays != binding.pastDaysCheckBox.isChecked() ||
            scheduleSettings.isShowEmptyDays != binding.emptyDaysCheckBox.isChecked() ||
            scheduleSettings.isShowShortSchedule != binding.shortScheduleCheckBox.isChecked() ||
            scheduleSettings.isAutoUpdate != binding.autoUpdateCheckBox.isChecked()
        ) {
            binding.saveButton.animate().setDuration(300).alpha(1f)
            binding.saveButton.isEnabled = true
        } else {
            binding.saveButton.animate().setDuration(300).alpha(0f)
            binding.saveButton.isEnabled = false
        }
    }

}


