package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleUpdateHistoryBinding
import com.bsuir.bsuirschedule.domain.models.ScheduleSubjectHistory
import com.bsuir.bsuirschedule.presentation.adapters.ScheduleDaysUpdateHistoryAdapter
import com.bsuir.bsuirschedule.presentation.dialogs.SubjectDialog
import com.bsuir.bsuirschedule.presentation.viewModels.ScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleUpdateHistoryFragment : Fragment() {

    private val groupScheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleUpdateHistoryBinding.inflate(inflater)

        val showSubjectDialog = { subject: ScheduleSubjectHistory ->
            val subjectDialog = SubjectDialog(subject.scheduleSubject, null)
            subjectDialog.isCancelable = true
            subjectDialog.show(parentFragmentManager, "subjectDialog")
        }

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleUpdateHistoryFragment_to_mainScheduleFragment)
        }

        val adapter = ScheduleDaysUpdateHistoryAdapter(context!!)
        binding.scheduleUpdateHistoryRecycler.layoutManager = LinearLayoutManager(context)
        binding.scheduleUpdateHistoryRecycler.adapter = adapter

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) return@observe

            binding.scheduleHeaderView.setTitle(schedule.getTitle())
            binding.scheduleHeaderView.setDescription(schedule.getDescription())
            if (!schedule.isGroup()) {
                binding.scheduleHeaderView.setImage(schedule.employee.photoLink)
            } else {
                binding.scheduleHeaderView.setImage(R.drawable.ic_group_placeholder)
            }

            if (schedule.updateHistorySchedule.size > 0) {
                binding.noUpdatedPlaceholder.visibility = View.GONE
                binding.scheduleUpdateHistoryRecycler.visibility = View.VISIBLE
                adapter.updateSchedule(schedule.updateHistorySchedule, schedule.isGroup(), showSubjectDialog)
                binding.scheduleUpdateHistoryRecycler.adapter = adapter
            } else {
                adapter.updateSchedule(ArrayList(), false, null)
                binding.noUpdatedPlaceholder.visibility = View.VISIBLE
                binding.scheduleUpdateHistoryRecycler.visibility = View.GONE
            }
        }

        return binding.root
    }

}


