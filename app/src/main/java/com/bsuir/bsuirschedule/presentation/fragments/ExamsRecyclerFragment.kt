package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentExamsRecyclerBinding
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.presentation.adapters.MainScheduleAdapter
import com.bsuir.bsuirschedule.presentation.dialogs.SubjectDialog
import com.bsuir.bsuirschedule.presentation.dialogs.UploadScheduleDialog
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ExamsRecyclerFragment : Fragment() {

    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentExamsRecyclerBinding.inflate(inflater)

        val onSubmitUploadSchedule = { savedSchedule: SavedSchedule ->
            groupScheduleVM.getOrUploadSchedule(savedSchedule)
        }

        val onSubjectSourceClick = { savedSchedule: SavedSchedule ->
            val uploadScheduleDialog = UploadScheduleDialog(savedSchedule, onSubmitUploadSchedule)
            uploadScheduleDialog.isCancelable = true
            uploadScheduleDialog.show(parentFragmentManager, "uploadScheduleDialog")
        }

        val onLongClickSubject = { subject: ScheduleSubject, subjectView: View ->
            val subjectDialog = SubjectDialog(subject, onSubjectSourceClick)
            subjectDialog.isCancelable = true
            subjectDialog.show(parentFragmentManager, "subjectDialog")
        }

        val showSubjectDialog = { subject: ScheduleSubject ->
            val subjectDialog = SubjectDialog(subject, onSubjectSourceClick)
            subjectDialog.isCancelable = true
            subjectDialog.show(parentFragmentManager, "subjectDialog")
        }

        val adapter = MainScheduleAdapter(context!!)
        adapter.setExams(true)
        binding.scheduleDailyRecycler.layoutManager = LinearLayoutManager(context)
        binding.scheduleDailyRecycler.adapter = adapter

        groupScheduleVM.examsScheduleStatus.observe(viewLifecycleOwner) { groupSchedule ->
            if (groupSchedule != null) {
                binding.placeholder.visibility = View.GONE
                binding.scheduleDailyRecycler.visibility = View.VISIBLE
                adapter.updateSchedule(groupSchedule.examsSchedule, groupSchedule.isGroup(), showSubjectDialog, onLongClickSubject)
                binding.scheduleDailyRecycler.adapter = adapter
                binding.scheduleDailyRecycler.alpha = 0f
                binding.scheduleDailyRecycler.animate().alpha(1f).setDuration(300).start()
            } else {
                adapter.updateSchedule(ArrayList(), false, null, null)
                binding.placeholder.visibility = View.VISIBLE
                binding.scheduleDailyRecycler.visibility = View.GONE
            }
        }

        return binding.root
    }

}