package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSubjectAddBinding
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.presentation.dialogs.AddScheduleItemDialog
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

        scheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) return@observe

            binding.addCustomSubjectView.setGroupType(!schedule.isGroup())
            binding.addCustomSubjectView.setSubgroups(schedule.subgroups)
        }

        val onSourceSelect = { savedSchedule: SavedSchedule ->
            binding.addCustomSubjectView.setSourceScheduleItem(savedSchedule.getName())
        }

        binding.addCustomSubjectView.setOnCancelListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectAddFragment_to_mainScheduleFragment)
        }

        binding.addCustomSubjectView.setOnAddSourceScheduleListener { isGroup ->
            val addScheduleItemDialog = AddScheduleItemDialog(isGroup, onSourceSelect)
            addScheduleItemDialog.isCancelable = true
            addScheduleItemDialog.show(parentFragmentManager, "AddSourceScheduleListener")
        }

        binding.addCustomSubjectView.setOnSelectScheduleListener { newSubject, sourceItemsText ->
            scheduleVM.addCustomSubject(newSubject, sourceItemsText)
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectAddFragment_to_mainScheduleFragment)
        }

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectAddFragment_to_mainScheduleFragment)
        }

        return binding.root
    }
}