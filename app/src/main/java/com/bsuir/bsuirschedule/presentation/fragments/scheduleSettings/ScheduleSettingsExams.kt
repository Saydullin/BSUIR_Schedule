package com.bsuir.bsuirschedule.presentation.fragments.scheduleSettings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSettingsExamsBinding
import com.bsuir.bsuirschedule.domain.models.ScheduleSettings
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
            val settings = schedule.settings

            binding.emptyDaysCheckBox.isChecked = settings.isShowEmptyExamDays
            binding.pastDaysCheckBox.isChecked = settings.isShowPastExamDays
        }

        binding.emptyDaysCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val schedule = groupScheduleVM.scheduleStatus.value ?: return@setOnCheckedChangeListener
            schedule.settings.isShowEmptyExamDays = isChecked
            groupScheduleVM.updateScheduleSettings(schedule.id, schedule.settings)
        }

        binding.pastDaysCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val schedule = groupScheduleVM.scheduleStatus.value ?: return@setOnCheckedChangeListener
            schedule.settings.isShowPastExamDays = isChecked
            groupScheduleVM.updateScheduleSettings(schedule.id, schedule.settings)
        }

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