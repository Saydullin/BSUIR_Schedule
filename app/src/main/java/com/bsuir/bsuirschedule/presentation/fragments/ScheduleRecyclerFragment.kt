package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleRecyclerBinding
import com.bsuir.bsuirschedule.domain.models.DeleteSubjectSettings
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.LoadingStatus
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.presentation.adapters.MainScheduleAdapter
import com.bsuir.bsuirschedule.presentation.dialogs.*
import com.bsuir.bsuirschedule.presentation.popupMenu.ScheduleSubjectPopupMenu
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
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

        val onSubmitUploadSchedule = { savedSchedule: SavedSchedule ->
            groupScheduleVM.getOrUploadSchedule(savedSchedule)
        }

        val onSubjectSourceClick = { savedSchedule: SavedSchedule ->
            val uploadScheduleDialog = UploadScheduleDialog(savedSchedule, onSubmitUploadSchedule)
            uploadScheduleDialog.isCancelable = true
            uploadScheduleDialog.show(parentFragmentManager, "uploadScheduleDialog")
        }

        val onEditScheduleSubject = { scheduleSubject: ScheduleSubject ->
            groupScheduleVM.setActiveSubject(scheduleSubject)
            Navigation.findNavController(binding.root).navigate(R.id.action_mainScheduleFragment_to_scheduleSubjectEditFragment)
        }

        val onIgnoreScheduleSubject = { scheduleSubject: ScheduleSubject, isIgnore: Boolean ->
            groupScheduleVM.ignoreSubject(scheduleSubject, isIgnore)
        }

        val onDeleteSubmitScheduleSubject = { scheduleSubject: ScheduleSubject, deleteSettings: DeleteSubjectSettings ->
            groupScheduleVM.deleteSubject(scheduleSubject, deleteSettings)
        }

        val onDeleteScheduleSubject = { scheduleSubject: ScheduleSubject ->
            val deleteSubjectDialog = DeleteSubjectDialog(scheduleSubject = scheduleSubject, agreeCallback = onDeleteSubmitScheduleSubject)
            deleteSubjectDialog.show(parentFragmentManager, "WarningDialog")
        }

        val onLongPressSubject = { subject: ScheduleSubject, subjectView: View ->
            val popupMenu = ScheduleSubjectPopupMenu(
                context = context!!,
                scheduleSubject = subject,
                edit = onEditScheduleSubject,
                isIgnore = onIgnoreScheduleSubject,
                delete = onDeleteScheduleSubject
            ).initPopupMenu(subjectView)

            popupMenu.show()
        }

        val showSubjectDialog = { subject: ScheduleSubject ->
            val subjectDialog = SubjectDialog(subject, onSubjectSourceClick)
            subjectDialog.isCancelable = true
            subjectDialog.show(parentFragmentManager, "subjectDialog")
        }
        val adapter = MainScheduleAdapter(context!!)
        binding.scheduleDailyRecycler.layoutManager = LinearLayoutManager(context)
        binding.scheduleDailyRecycler.adapter = adapter

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

        groupScheduleVM.dataLoadingStatus.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.scheduleDailyRecycler.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.scheduleDailyRecycler.visibility = View.VISIBLE
            }
        }

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { groupSchedule ->
            if (groupSchedule == null) return@observe
            if (groupSchedule.schedules.size > 0) {
                val scrollState = (binding.scheduleDailyRecycler.layoutManager as LinearLayoutManager).onSaveInstanceState()
                binding.placeholder.visibility = View.GONE
                binding.scheduleDailyRecycler.visibility = View.VISIBLE
                adapter.setShortSchedule(groupSchedule.settings.schedule.isShowShortSchedule)
                adapter.updateSchedule(groupSchedule.schedules, groupSchedule.isGroup(), showSubjectDialog, onLongPressSubject)
                (binding.scheduleDailyRecycler.layoutManager as LinearLayoutManager).onRestoreInstanceState(scrollState)
                binding.scheduleDailyRecycler.adapter = adapter
            } else {
                adapter.updateSchedule(ArrayList(), false, null, null)
                binding.placeholder.visibility = View.VISIBLE
                binding.scheduleDailyRecycler.visibility = View.GONE
            }
        }

        return binding.root
    }

}


