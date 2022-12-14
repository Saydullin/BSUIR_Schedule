package com.bsuir.bsuirschedule.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ActiveScheduleDialogBinding
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.presentation.utils.SubjectManager

class SavedScheduleDialog(
    private val schedule: Schedule,
    private val update: (savedSchedule: SavedSchedule) -> Unit,
    private val delete: (savedSchedule: SavedSchedule) -> Unit
): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ActiveScheduleDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        val lastUpdateText = resources.getString(R.string.last_update, schedule.getLastUpdateText())
        val courseText = resources.getString(R.string.course)
        val moreText = resources.getString(R.string.more)
        val selectedSubgroup = schedule.settings.subgroup.selectedNum
        binding.scheduleSubgroup.text = if (selectedSubgroup == 0) {
            resources.getString(R.string.selected_all_subgroups)
        } else {
            resources.getString(R.string.selected_subgroup, selectedSubgroup)
        }

        val examsDatePeriod = if (!schedule.isExamsNotExist()) {
            resources.getString(
                R.string.exams_date_period,
                schedule.getDateText(schedule.startExamsDate),
                schedule.getDateText(schedule.endExamsDate)
            )
        } else {
            resources.getString(R.string.exams_empty_date_period)
        }
        binding.examsDate.text = examsDatePeriod

        val scheduleDatePeriod = if (!schedule.isScheduleNotExist()) {
            resources.getString(
                R.string.schedule_date_period,
                schedule.getDateText(schedule.startDate),
                schedule.getDateText(schedule.endDate)
            )
        } else {
            resources.getString(R.string.schedule_empty_date_period)
        }
        binding.scheduleDate.text = scheduleDatePeriod

        if (schedule.isGroup()) {
            val group = schedule.group
            Glide.with(binding.schedule.image)
                .load(R.drawable.ic_group_placeholder)
                .into(binding.schedule.image)
            binding.schedule.title.text = group.name
            binding.schedule.departments.text = group.getFacultyAndSpecialityAbbr()
            binding.schedule.educationType.text = group.speciality?.educationForm?.name ?: ""
            binding.schedule.course.visibility = View.VISIBLE
            binding.schedule.course.text = "${group.course} $courseText"
            binding.scheduleSubtitles.text = group.getFacultyAndSpecialityFull()
            binding.lastUpdate.text = lastUpdateText
        } else {
            val employee = schedule.employee
            Glide.with(binding.schedule.image)
                .load(employee.photoLink)
                .into(binding.schedule.image)
            binding.schedule.title.text = employee.getFullName()
            binding.schedule.departments.text = employee.getRankAndDegree()
            binding.schedule.educationType.text = employee.getShortDepartments(moreText)
            binding.schedule.course.visibility = View.GONE
            if (employee.departmentsList.isNullOrEmpty()) {
                val noDepartments = resources.getString(R.string.active_schedule_no_departments)
                binding.scheduleSubtitles.text = noDepartments
            } else {
                binding.scheduleSubtitles.text = employee.getFullDepartments("\n\n")
            }
            binding.lastUpdate.text = lastUpdateText
        }

        if (schedule.subjectNow != null) {
            val subjectManager = SubjectManager(schedule.subjectNow!!, context!!)
            binding.scheduleLocationNow.text = subjectManager.getSubjectDate()
        } else {
            val subjectNowText = resources.getString(R.string.no_subject_now)
            binding.scheduleLocationNow.text = subjectNowText
        }

        binding.deleteButton.setOnClickListener {
            delete(schedule.toSavedSchedule())
            dismiss()
        }

        binding.updateButton.setOnClickListener {
            update(schedule.toSavedSchedule())
            dismiss()
        }

        return binding.root
    }

}


