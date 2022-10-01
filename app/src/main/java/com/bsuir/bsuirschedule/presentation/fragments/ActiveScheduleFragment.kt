package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.presentation.dialogs.SavedScheduleDialog
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.SavedSchedulesViewModel
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentActiveScheduleBinding
import org.koin.androidx.navigation.koinNavGraphViewModel
import java.util.*

class ActiveScheduleFragment : Fragment() {

    private val savedScheduleVM: SavedSchedulesViewModel by koinNavGraphViewModel(R.id.navigation)
    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentActiveScheduleBinding.inflate(inflater)

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) return@observe

            if (schedule.isGroup()) {
                val group = schedule.group
                val courseText = getString(R.string.course)
                Glide.with(binding.scheduleImage)
                    .load(R.drawable.ic_group_placeholder)
                    .into(binding.scheduleImage)
                binding.scheduleCourse.visibility = View.VISIBLE
                binding.scheduleCourse.text = "${group.course} $courseText"
                binding.activeScheduleTitle.text = group.name
                binding.scheduleSubtitleLeft.text = group.getFacultyAndSpecialityAbbr()
                binding.scheduleSubtitleRight.text = group.speciality?.educationForm?.name ?: ""
            } else {
                val employee = schedule.employee
                binding.scheduleCourse.text = ""
                Glide.with(binding.scheduleImage)
                    .load(employee.photoLink)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.scheduleImage)
                binding.activeScheduleTitle.text = employee.getFullName()
                binding.scheduleSubtitleLeft.text = employee.getRankAndDegree()
                binding.scheduleCourse.visibility = View.GONE
                if (!employee.departmentsList.isNullOrEmpty()) {
                    binding.scheduleSubtitleRight.text = employee.getShortDepartments(getString(R.string.more))
                }
            }

            if (schedule.selectedSubgroup == 0) {
                binding.subgroup.text = resources.getString(R.string.all_subgroups_short)
            } else {
                binding.subgroup.text = schedule.selectedSubgroup.toString()
            }
        }

        val deleteSchedule = { savedSchedule: SavedSchedule ->
            savedScheduleVM.deleteSchedule(savedSchedule)
            groupScheduleVM.deleteSchedule(savedSchedule)
        }

        val updateSchedule = { savedSchedule: SavedSchedule ->
            savedSchedule.lastUpdateTime = Date().time
            savedScheduleVM.setActiveSchedule(savedSchedule)
            if (savedSchedule.isGroup) {
                groupScheduleVM.getGroupScheduleAPI(savedSchedule.group)
            } else {
                groupScheduleVM.getEmployeeScheduleAPI(savedSchedule.employee)
            }
        }

        binding.root.setOnClickListener {
            val activeSchedule = groupScheduleVM.scheduleStatus.value ?: return@setOnClickListener
            val savedScheduleDialog = SavedScheduleDialog(
                schedule = activeSchedule,
                delete = deleteSchedule,
                update = updateSchedule)
            savedScheduleDialog.isCancelable = true
            savedScheduleDialog.show(parentFragmentManager, "savedScheduleDialog")
        }

        return binding.root
    }

}


