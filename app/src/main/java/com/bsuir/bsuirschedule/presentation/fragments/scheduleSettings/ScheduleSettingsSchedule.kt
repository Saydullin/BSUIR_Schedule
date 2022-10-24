package com.bsuir.bsuirschedule.presentation.fragments.scheduleSettings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
            binding.shortScheduleCheckBox.isChecked = settings.isShowShortSchedule
            binding.pastDaysAmountEditText.setText(settings.pastDaysNumber.toString())
            binding.pastDaysAmountEditText.setSelection(binding.pastDaysAmountEditText.length())
        }

        binding.autoUpdateCheckBox.setOnCheckedChangeListener { _, _->
            validateChanges(binding)
        }

        binding.emptyDaysCheckBox.setOnCheckedChangeListener { _, _ ->
            validateChanges(binding)
        }

        binding.shortScheduleCheckBox.setOnCheckedChangeListener { _, _ ->
            validateChanges(binding)
        }

        binding.resetButton.setOnClickListener {
            val schedule = groupScheduleVM.getActiveSchedule() ?: return@setOnClickListener
            val scheduleSettings = schedule.settings
            scheduleSettings.schedule = ScheduleSettings.empty.schedule
            groupScheduleVM.updateScheduleSettings(schedule.id, scheduleSettings)
        }

        binding.pastDaysAmountEditText.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(s: Editable) {
                validateChanges(binding)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })

        binding.pastDaysCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.pastDaysShowAmount.visibility = View.VISIBLE
            } else {
                binding.pastDaysShowAmount.visibility = View.GONE
            }
            validateChanges(binding)
        }

        binding.saveButton.setOnClickListener {
            val schedule = groupScheduleVM.getActiveSchedule() ?: return@setOnClickListener
            val scheduleSettings = schedule.settings
            binding.saveButton.animate().setDuration(300).alpha(0f)
            binding.saveButton.isEnabled = false
            scheduleSettings.schedule.isShowPastDays = binding.pastDaysCheckBox.isChecked
            if (binding.pastDaysAmountEditText.text.isDigitsOnly()) {
                scheduleSettings.schedule.pastDaysNumber = binding.pastDaysAmountEditText.text.toString().toInt()
            } else {
                scheduleSettings.schedule.pastDaysNumber = 0
            }
            scheduleSettings.schedule.isShowEmptyDays = binding.emptyDaysCheckBox.isChecked
            scheduleSettings.schedule.isShowShortSchedule = binding.shortScheduleCheckBox.isChecked
            scheduleSettings.schedule.isAutoUpdate = binding.autoUpdateCheckBox.isChecked
            groupScheduleVM.updateScheduleSettings(schedule.id, scheduleSettings)
        }

        return binding.root
    }

    private fun validateChanges(binding: FragmentScheduleSettingsScheduleBinding) {
        val schedule = groupScheduleVM.getActiveSchedule() ?: return
        val scheduleSettings = schedule.settings.schedule

        if (
            (binding.pastDaysAmountEditText.text.isNotEmpty() && binding.pastDaysAmountEditText.text.isDigitsOnly() &&
            binding.pastDaysAmountEditText.text.toString().toInt() != scheduleSettings.pastDaysNumber) ||
            scheduleSettings.isShowPastDays != binding.pastDaysCheckBox.isChecked ||
            scheduleSettings.isShowEmptyDays != binding.emptyDaysCheckBox.isChecked ||
            scheduleSettings.isShowShortSchedule != binding.shortScheduleCheckBox.isChecked ||
            scheduleSettings.isAutoUpdate != binding.autoUpdateCheckBox.isChecked
        ) {
            binding.saveButton.animate().setDuration(300).alpha(1f)
            binding.saveButton.isEnabled = true
        } else {
            binding.saveButton.animate().setDuration(300).alpha(0f)
            binding.saveButton.isEnabled = false
        }
    }

}


