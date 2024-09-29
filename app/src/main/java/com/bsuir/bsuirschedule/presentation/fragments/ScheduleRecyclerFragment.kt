package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.data.logger.Logger
import com.bsuir.bsuirschedule.databinding.FragmentScheduleRecyclerBinding
import com.bsuir.bsuirschedule.domain.models.ChangeSubjectSettings
import com.bsuir.bsuirschedule.domain.models.EmployeeSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.LoadingStatus
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.ScheduleTerm
import com.bsuir.bsuirschedule.domain.models.scheduleSettings.ScheduleSettings
import com.bsuir.bsuirschedule.notification.ScheduleNotification
import com.bsuir.bsuirschedule.presentation.adapters.MainScheduleAdapter
import com.bsuir.bsuirschedule.presentation.dialogs.*
import com.bsuir.bsuirschedule.presentation.popupMenu.ScheduleSubjectPopupMenu
import com.bsuir.bsuirschedule.presentation.utils.ViewVisible
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
        var scheduleTerm = ScheduleTerm.NOTHING

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

        val onEditScheduleSubject = { scheduleSubject: ScheduleSubject ->
            groupScheduleVM.setActiveSubject(scheduleSubject)
            Navigation.findNavController(binding.root).navigate(R.id.action_mainScheduleFragment_to_scheduleSubjectEditFragment)
        }

        val onIgnoreScheduleSubject = { scheduleSubject: ScheduleSubject, isIgnore: Boolean ->
            groupScheduleVM.ignoreSubject(scheduleSubject, isIgnore, scheduleTerm)
        }

        val onDeleteSubmitScheduleSubject = { scheduleSubject: ScheduleSubject, deleteSettings: ChangeSubjectSettings ->
            groupScheduleVM.deleteSubject(scheduleSubject, deleteSettings, scheduleTerm)
        }

        val onDeleteScheduleSubject = { scheduleSubject: ScheduleSubject ->
            val deleteSubjectDialog = DeleteSubjectDialog(scheduleSubject = scheduleSubject, agreeCallback = onDeleteSubmitScheduleSubject)
            deleteSubjectDialog.show(parentFragmentManager, "WarningDialog")
        }

        val onLongPressSubject = { subject: ScheduleSubject, subjectView: View ->
            val popupMenu = ScheduleSubjectPopupMenu(
                context = requireContext(),
                scheduleSubject = subject,
                edit = onEditScheduleSubject,
                isIgnore = onIgnoreScheduleSubject,
                delete = onDeleteScheduleSubject
            ).initPopupMenu(subjectView)

            popupMenu.show()
        }

        val showSubjectDialog = { subject: ScheduleSubject ->
            val subjectDialog = SubjectDialog(
                subject = subject,
                onClickSubjectSource = onSubjectSourceClick,
            )
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
                if (recyclerViewHeight < 25 || recyclerViewHeight >= recyclerViewsAmount - 15) {
                    binding.scrollDownButton.animate().translationY(300f).alpha(0f).duration = 200
                } else if (recyclerViewHeight > 25) {
                    binding.scrollDownButton.animate().translationY(0f).alpha(1f).duration = 200
                }
            }
        }
        binding.scheduleDailyRecycler.addOnScrollListener(scrollListener)

        binding.scrollUpButton.setOnClickListener {
            binding.scheduleDailyRecycler.smoothScrollToPosition(0)
        }

        binding.scrollDownButton.setOnClickListener {
            val itemCount = recyclerViewLayoutManager.itemCount
            binding.scheduleDailyRecycler.smoothScrollToPosition(itemCount - 1)
        }

        groupScheduleVM.errorStatus.observe(viewLifecycleOwner) { errorStatus ->
            if (errorStatus != null) {
                val stateDialog = StateDialog(errorStatus)
                stateDialog.isCancelable = true
                stateDialog.show(parentFragmentManager, "ErrorDialog")
                groupScheduleVM.closeError()
                Logger(requireContext()).log("${errorStatus.type} = ${errorStatus.message}")
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
            var isEmptyList = false
            scheduleTerm = schedule.settings.term.selectedTerm

            val scrollState = (binding.scheduleDailyRecycler.layoutManager as LinearLayoutManager).onSaveInstanceState()
            binding.noSubjectsPlaceholder.visibility = View.GONE
            binding.scheduleDailyRecycler.visibility = View.VISIBLE
            binding.scrollUpButton.visibility = View.VISIBLE
            adapter.setShortSchedule(schedule.settings.schedule.isShowShortSchedule)
            when(schedule.settings.term.selectedTerm) {
                ScheduleTerm.CURRENT_SCHEDULE -> {
                    isEmptyList = schedule.schedules.isEmpty()
                    adapter.updateScheduleData(schedule.schedules, schedule.isGroup())
                }
                ScheduleTerm.PREVIOUS_SCHEDULE -> {
                    isEmptyList = schedule.previousSchedules.isEmpty()
                    adapter.updateScheduleData(schedule.previousSchedules, schedule.isGroup())
                }
                ScheduleTerm.SESSION -> {
                    Log.e("sady", "exams showing")
                    isEmptyList = schedule.examsSchedule.isEmpty()
                    adapter.updateScheduleData(schedule.examsSchedule, schedule.isGroup())
                }
                else -> {
                    adapter.updateScheduleData(arrayListOf(), schedule.isGroup())
                }
            }
            (binding.scheduleDailyRecycler.layoutManager as LinearLayoutManager).onRestoreInstanceState(scrollState)
            binding.scheduleDailyRecycler.adapter = adapter
            binding.scheduleDailyRecycler.alpha = 0f
            binding.scheduleDailyRecycler.animate().alpha(1f).duration = 300
            if (isEmptyList) {
                binding.noSchedulePlaceholder.noSchedulePlaceholderCaption.visibility = ViewVisible.ifIs(
                    schedule.startDate.isNotEmpty() && schedule.endDate.isNotEmpty()
                )
                adapter.updateScheduleData(ArrayList())
                binding.noSubjectsPlaceholder.visibility = View.VISIBLE
                binding.scheduleDailyRecycler.visibility = View.GONE
                binding.scrollUpButton.visibility = View.GONE
                binding.scrollDownButton.visibility = View.GONE
            }
        }

        return binding.root
    }

}


