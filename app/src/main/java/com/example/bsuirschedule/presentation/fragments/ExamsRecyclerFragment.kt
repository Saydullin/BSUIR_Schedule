package com.example.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bsuirschedule.R
import com.example.bsuirschedule.databinding.FragmentExamsRecyclerBinding
import com.example.bsuirschedule.domain.models.ScheduleSubject
import com.example.bsuirschedule.presentation.adapters.MainScheduleAdapter
import com.example.bsuirschedule.presentation.dialogs.SubjectDialog
import com.example.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ExamsRecyclerFragment : Fragment() {

    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentExamsRecyclerBinding.inflate(inflater)

        val showSubjectDialog = { subject: ScheduleSubject ->
            val subjectDialog = SubjectDialog(subject)
            subjectDialog.isCancelable = true
            subjectDialog.show(parentFragmentManager, "subjectDialog")
        }

        val adapter = MainScheduleAdapter(context!!, ArrayList(), false, showSubjectDialog, true)

        groupScheduleVM.examsScheduleStatus.observe(viewLifecycleOwner) { groupSchedule ->
            adapter.updateSchedule(groupSchedule.schedules)
            adapter.isGroupSchedule = groupSchedule.isGroup ?: false
            binding.scheduleDailyRecycler.adapter = adapter
            binding.scheduleDailyRecycler.layoutManager = LinearLayoutManager(context)
            binding.scheduleDailyRecycler.alpha = 0f
            binding.scheduleDailyRecycler.animate().alpha(1f).setDuration(300).start()
        }

        return binding.root
    }

}