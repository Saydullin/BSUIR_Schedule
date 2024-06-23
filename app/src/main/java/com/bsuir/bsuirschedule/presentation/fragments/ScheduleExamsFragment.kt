package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleExamsBinding
import com.bsuir.bsuirschedule.domain.models.EmployeeSubject
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.presentation.adapters.MainScheduleAdapter
import com.bsuir.bsuirschedule.presentation.dialogs.SubjectDialog
import com.bsuir.bsuirschedule.presentation.dialogs.UploadScheduleDialog
import com.bsuir.bsuirschedule.presentation.viewModels.ScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleExamsFragment : Fragment() {

    private val groupScheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleExamsBinding.inflate(inflater)
        var scheduleSettings: ScheduleSettings? = null

        val onSubmitUploadSchedule = { savedSchedule: SavedSchedule ->
            groupScheduleVM.getOrUploadSchedule(savedSchedule)
        }
        val onSubjectSourceClick = { savedSchedule: SavedSchedule, employeeSubject: EmployeeSubject? ->
            val uploadScheduleDialog = UploadScheduleDialog(
                savedSchedule = savedSchedule,
                employeeSubject = employeeSubject,
                onUploadSubmit = onSubmitUploadSchedule,
            )
            uploadScheduleDialog.isCancelable = true
            uploadScheduleDialog.show(parentFragmentManager, "uploadScheduleDialog")
        }
        val showSubjectDialog = { subject: ScheduleSubject ->
            val subjectDialog = SubjectDialog(
                subject = subject,
                onClickSubjectSource = onSubjectSourceClick,
                scheduleSettings = scheduleSettings
            )
            subjectDialog.isCancelable = true
            subjectDialog.show(parentFragmentManager, "subjectDialog")
        }

        val adapter = MainScheduleAdapter(
            requireContext(),
            arrayListOf(),
            true,
            showSubjectDialog,
            null,
        )
        val recyclerViewLayoutManager = LinearLayoutManager(context)
        binding.scheduleExamsRecycler.layoutManager = recyclerViewLayoutManager
        binding.scheduleExamsRecycler.adapter = adapter

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) {
            binding.scheduleHeaderView.setSavedSchedule(it.toSavedSchedule())
        }

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            scheduleSettings = schedule.settings
            adapter.setShortSchedule(schedule.settings.schedule.isShowShortSchedule)
            adapter.updateScheduleData(schedule.examsSchedule, schedule.isGroup())
            binding.scheduleExamsRecycler.adapter = adapter
        }

        return binding.root
    }

}