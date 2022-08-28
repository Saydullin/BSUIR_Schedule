package com.example.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bsuirschedule.R
import com.example.bsuirschedule.databinding.FragmentScheduleRecyclerBinding
import com.example.bsuirschedule.domain.models.ScheduleSubject
import com.example.bsuirschedule.domain.models.LoadingStatus
import com.example.bsuirschedule.presentation.adapters.MainScheduleAdapter
import com.example.bsuirschedule.presentation.dialogs.LoadingDialog
import com.example.bsuirschedule.presentation.dialogs.StateDialog
import com.example.bsuirschedule.presentation.dialogs.SubjectDialog
import com.example.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleRecyclerFragment : Fragment() {

    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleRecyclerBinding.inflate(inflater)
        val loadingStatus = LoadingStatus(LoadingStatus.LOAD_SCHEDULE)
        val dialog = LoadingDialog(loadingStatus)
        dialog.isCancelable = false

        groupScheduleVM.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (errorStatus != null) {
                val stateDialog = StateDialog(errorStatus)
                stateDialog.isCancelable = true
                stateDialog.show(parentFragmentManager, "ErrorDialog")
                groupScheduleVM.closeError()
            }
        }

        groupScheduleVM.loadingStatus.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                dialog.show(parentFragmentManager, "LoadingDialog")
            } else {
                if (dialog.dialog?.isShowing == true) {
                    dialog.dismiss()
                }
            }
        }

        val showSubjectDialog = { subject: ScheduleSubject ->
            val subjectDialog = SubjectDialog(subject)
            subjectDialog.isCancelable = true
            subjectDialog.show(parentFragmentManager, "subjectDialog")
        }

        val adapter = MainScheduleAdapter(context!!, ArrayList(), false, showSubjectDialog)

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { groupSchedule ->
            if (groupSchedule.schedules.size > 0) {
                binding.placeholder.visibility = View.GONE
                binding.scheduleDailyRecycler.visibility = View.VISIBLE
                adapter.updateSchedule(groupSchedule.schedules)
                adapter.isGroupSchedule = groupSchedule.isGroup ?: false
                binding.scheduleDailyRecycler.adapter = adapter
                binding.scheduleDailyRecycler.layoutManager = LinearLayoutManager(context)
                binding.scheduleDailyRecycler.alpha = 0f
                binding.scheduleDailyRecycler.animate().alpha(1f).setDuration(300).start()
            } else {
                binding.placeholder.visibility = View.VISIBLE
                binding.scheduleDailyRecycler.visibility = View.GONE
            }
        }

        return binding.root
    }

}


