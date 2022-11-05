package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.presentation.dialogs.SavedScheduleDialog
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.SavedSchedulesViewModel
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentActiveScheduleBinding
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.presentation.dialogs.WarningDialog
import com.bsuir.bsuirschedule.presentation.utils.SubjectManager
import com.bsuir.bsuirschedule.presentation.viewModels.EmployeeItemsViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.GroupItemsViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel
import java.util.*

class ActiveScheduleFragment : Fragment() {

    private val groupItemsVM: GroupItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val employeeItemsVM: EmployeeItemsViewModel by koinNavGraphViewModel(R.id.navigation)
    private val savedScheduleVM: SavedSchedulesViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentActiveScheduleBinding.inflate(inflater)

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) return@observe
            val selectedSubgroup = schedule.settings.subgroup.selectedNum

            if (schedule.isGroup()) {
                val group = schedule.group
                val courseText = getString(R.string.course)
                Glide.with(binding.scheduleImage)
                    .load(R.drawable.ic_group_placeholder)
                    .into(binding.scheduleImage)
                binding.scheduleCourse.text = "${group.course} $courseText"
                binding.activeScheduleTitle.text = group.getTitleOrName()
            } else {
                val employee = schedule.employee
                binding.scheduleCourse.text = ""
                Glide.with(binding.scheduleImage)
                    .load(employee.photoLink)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.scheduleImage)
                binding.activeScheduleTitle.text = employee.getTitleOrFullName()
            }

            setCurrentSubject(binding.currentSubjectInfo, schedule.subjectNow)

            if (selectedSubgroup == 0) {
                binding.subgroup.text = resources.getString(R.string.all_subgroups_short)
            } else {
                binding.subgroup.text = selectedSubgroup.toString()
            }
        }

        val deleteSchedule = { savedSchedule: SavedSchedule ->
            savedScheduleVM.deleteSchedule(savedSchedule)
            groupScheduleVM.deleteSchedule(savedSchedule)
            if (savedSchedule.isGroup) {
                savedSchedule.group.isSaved = false
                groupItemsVM.saveGroupItem(savedSchedule.group)
            } else {
                savedSchedule.employee.isSaved = false
                employeeItemsVM.saveEmployeeItem(savedSchedule.employee)
            }
        }

        val deleteWarning = { savedSchedule: SavedSchedule ->
            val warningDialog = WarningDialog(savedSchedule = savedSchedule, agreeCallback = deleteSchedule)
            warningDialog.show(parentFragmentManager, "WarningDialog")
        }

        val updateSchedule = { savedSchedule: SavedSchedule ->
            savedSchedule.lastUpdateTime = Date().time
            savedScheduleVM.setActiveSchedule(savedSchedule)
            if (savedSchedule.isGroup) {
                groupScheduleVM.getGroupScheduleAPI(savedSchedule.group, isUpdate = true)
            } else {
                groupScheduleVM.getEmployeeScheduleAPI(savedSchedule.employee, isUpdate = true)
            }
        }

        binding.root.setOnClickListener {
            val activeSchedule = groupScheduleVM.scheduleStatus.value ?: return@setOnClickListener
            val savedScheduleDialog = SavedScheduleDialog(
                schedule = activeSchedule,
                delete = deleteWarning,
                update = updateSchedule)
            savedScheduleDialog.isCancelable = true
            savedScheduleDialog.show(parentFragmentManager, "savedScheduleDialog")
        }

        return binding.root
    }

    private fun setCurrentSubject(currentSubjectInfo: TextView, subject: ScheduleSubject?) {
        if (subject != null) {
            val subjectManager = SubjectManager(subject, context!!)
            currentSubjectInfo.text = subjectManager.getSubjectDate()
        } else {
            val subjectNowText = resources.getString(R.string.no_subject_now)
            currentSubjectInfo.text = subjectNowText
        }
    }

}


