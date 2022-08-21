package com.example.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bsuirschedule.domain.models.SavedSchedule
import com.example.bsuirschedule.presentation.dialogs.SavedScheduleDialog
import com.example.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import com.example.bsuirschedule.presentation.viewModels.SavedSchedulesViewModel
import com.example.bsuirschedule.R
import com.example.bsuirschedule.databinding.FragmentActiveScheduleBinding
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
        var activeSchedule: SavedSchedule? = null // FIXME

        binding.exam.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_to_examsScheduleFragment)
        }

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) return@observe
            activeSchedule = schedule.toSavedSchedule()

            if (schedule.isGroup == true) {
                val group = schedule.group
                val courseText = getString(R.string.course)
                Glide.with(binding.scheduleImage)
                    .load(R.drawable.ic_group_placeholder)
                    .into(binding.scheduleImage)
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
                binding.scheduleSubtitleRight.text = employee.getShortDepartmentsAbbr()
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
            if (activeSchedule == null) return@setOnClickListener
            val savedScheduleDialog = SavedScheduleDialog(
                savedSchedule = activeSchedule!!,
                delete = deleteSchedule,
                update = updateSchedule)
            savedScheduleDialog.isCancelable = true
            savedScheduleDialog.show(parentFragmentManager, "savedScheduleDialog")
        }

        return binding.root
    }

}


