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
import com.bsuir.bsuirschedule.domain.models.ScheduleSettings
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
            val settings = schedule.settings

            binding.autoUpdateCheckBox.isChecked = settings.isAutoUpdate
            binding.emptyDaysCheckBox.isChecked = settings.isShowEmptyDays
            binding.pastDaysCheckBox.isChecked = settings.isShowPastDays
            binding.pastDaysAmountEditText.setText(settings.pastDaysNumber.toString())
            binding.pastDaysAmountEditText.setSelection(binding.pastDaysAmountEditText.length())
        }

        binding.autoUpdateCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val schedule = groupScheduleVM.scheduleStatus.value ?: return@setOnCheckedChangeListener
            schedule.settings.isAutoUpdate = isChecked
            groupScheduleVM.updateScheduleSettings(schedule.id, schedule.settings)
        }

        binding.emptyDaysCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val schedule = groupScheduleVM.scheduleStatus.value ?: return@setOnCheckedChangeListener
            schedule.settings.isShowEmptyDays = isChecked
            groupScheduleVM.updateScheduleSettings(schedule.id, schedule.settings)
        }

        binding.pastDaysCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val schedule = groupScheduleVM.scheduleStatus.value ?: return@setOnCheckedChangeListener
            schedule.settings.isShowPastDays = isChecked
            groupScheduleVM.updateScheduleSettings(schedule.id, schedule.settings)

            if (isChecked) {
                binding.pastDaysShowAmount.visibility = View.VISIBLE
            } else {
                binding.pastDaysShowAmount.visibility = View.GONE
            }
        }

        binding.pastDaysAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) return
                if (s.isDigitsOnly()) {
                    val schedule = groupScheduleVM.scheduleStatus.value ?: return
                    if (schedule.settings.pastDaysNumber != s.toString().toInt()) {
                        schedule.settings.pastDaysNumber = s.toString().toInt()
                        groupScheduleVM.updateScheduleSettings(schedule.id, schedule.settings)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.resetButton.setOnClickListener {
            val schedule = groupScheduleVM.scheduleStatus.value ?: return@setOnClickListener
            groupScheduleVM.updateScheduleSettings(schedule.id, ScheduleSettings.empty)
            val resetText = getString(R.string.reset_schedule_settings)
            Toast.makeText(context, resetText, Toast.LENGTH_SHORT).show()
        }

        binding.saveButton.setOnClickListener {
            val savedText = getString(R.string.save_schedule_settings)
            Toast.makeText(context, savedText, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}


