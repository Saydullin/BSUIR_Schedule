package com.bsuir.bsuirschedule.presentation.fragments.scheduleSettings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSettingsBinding
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.presentation.dialogs.AlertDialog
import com.bsuir.bsuirschedule.presentation.viewModels.ScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleSettingsFragment : Fragment() {

    private val groupScheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleSettingsBinding.inflate(inflater)

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) return@observe
            if (schedule.isGroup()) {
                binding.scheduleHeaderView.setTitle(schedule.group.name ?: "")
                binding.scheduleHeaderView.setDescription(schedule.group.getFacultyAndSpecialityAbbr())
                binding.scheduleHeaderView.setImage(R.drawable.ic_group_placeholder)
            } else {
                binding.scheduleHeaderView.setTitle(schedule.employee.getFullName())
                binding.scheduleHeaderView.setDescription(schedule.employee.getRankAndDegree())
                binding.scheduleHeaderView.setImage(schedule.employee.photoLink ?: "")
            }
        }

        val onAgreeResetSettings = {
            val schedule = groupScheduleVM.getActiveSchedule()
            if (schedule != null) {
                val scheduleSettings = schedule.settings
                scheduleSettings.schedule = ScheduleSettings.empty.schedule
                groupScheduleVM.updateScheduleSettings(schedule.id, scheduleSettings)
            }
        }

        binding.resetButton.setOnClickListener {
            val alertTitle = getString(R.string.alert_reset_settings_title)
            val alertDialog = AlertDialog(requireContext(), alertTitle, onAgreeResetSettings)
            alertDialog.show()
        }

        groupScheduleVM.settingsUpdatedStatus.observe(viewLifecycleOwner) { isUpdated ->
            if (isUpdated) {
                val savedText = getString(R.string.save_schedule_settings)
                Toast.makeText(context, savedText, Toast.LENGTH_SHORT).show()
            }
        }

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSettingsFragment_to_mainScheduleFragment)
        }

        return binding.root
    }

}