package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleRecyclerBinding
import com.bsuir.bsuirschedule.domain.models.ChangeSubjectSettings
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.LoadingStatus
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.presentation.adapters.MainScheduleAdapter
import com.bsuir.bsuirschedule.presentation.dialogs.*
import com.bsuir.bsuirschedule.presentation.popupMenu.ScheduleSubjectPopupMenu
import com.bsuir.bsuirschedule.presentation.viewModels.ScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel
import kotlin.collections.ArrayList

class ScheduleRecyclerFragment : Fragment() {

    private val groupScheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleRecyclerBinding.inflate(inflater)
        val loadingStatus = LoadingStatus(LoadingStatus.LOAD_SCHEDULE)
        val scheduleLoadingDialog = LoadingDialog(loadingStatus)
        scheduleLoadingDialog.isCancelable = false
        binding.scheduleDailyRecycler.visibility = View.GONE

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

        val onDeleteSubmitScheduleSubject = { scheduleSubject: ScheduleSubject, deleteSettings: ChangeSubjectSettings ->
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
        val adapter = MainScheduleAdapter(
            requireContext(),
            arrayListOf(),
            false,
            showSubjectDialog,
            onLongPressSubject,
        )
        val recyclerViewLayoutManager = LinearLayoutManager(context)
        binding.scheduleDailyRecycler.layoutManager = recyclerViewLayoutManager
        binding.scheduleDailyRecycler.adapter = adapter

        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val recyclerViewHeight = recyclerViewLayoutManager.findLastVisibleItemPosition()
                val recyclerViewsAmount = recyclerViewLayoutManager.itemCount
                if (recyclerViewHeight < 15 || recyclerViewHeight >= recyclerViewsAmount - 1) {
                    binding.scrollUpButton.animate().translationY(300f).alpha(0f).duration = 200
                } else if (recyclerViewHeight > 15) {
                    binding.scrollUpButton.animate().translationY(0f).alpha(1f).duration = 200
                }
            }
        }
        binding.scheduleDailyRecycler.addOnScrollListener(scrollListener)

        binding.scrollUpButton.setOnClickListener {
            binding.scheduleDailyRecycler.smoothScrollToPosition(0)
        }

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
                scheduleLoadingDialog.show(parentFragmentManager, "LoadingDialog")
            } else {
                if (scheduleLoadingDialog.dialog?.isShowing == true) {
                    scheduleLoadingDialog.dismiss()
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

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) return@observe

            if (schedule.schedules.size > 0) {
                val scrollState = (binding.scheduleDailyRecycler.layoutManager as LinearLayoutManager).onSaveInstanceState()
                binding.noSubjectsPlaceholder.visibility = View.GONE
                binding.scheduleDailyRecycler.visibility = View.VISIBLE
                binding.scrollUpButton.visibility = View.VISIBLE
                adapter.setShortSchedule(schedule.settings.schedule.isShowShortSchedule)
                adapter.updateScheduleData(schedule.schedules, schedule.isGroup())
                (binding.scheduleDailyRecycler.layoutManager as LinearLayoutManager).onRestoreInstanceState(scrollState)
                binding.scheduleDailyRecycler.adapter = adapter
            } else {
                adapter.updateScheduleData(ArrayList())
                binding.noSubjectsPlaceholder.visibility = View.VISIBLE
                binding.scheduleDailyRecycler.visibility = View.GONE
                binding.scrollUpButton.visibility = View.GONE
            }
        }

        return binding.root
    }

}


