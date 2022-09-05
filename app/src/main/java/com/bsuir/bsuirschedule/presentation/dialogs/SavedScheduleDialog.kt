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

class SavedScheduleDialog(
    private val savedSchedule: SavedSchedule,
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
        val lastUpdateText = resources.getString(R.string.last_update, savedSchedule.getLastUpdateText())
        val courseText = resources.getString(R.string.course)

        if (savedSchedule.isGroup) {
            val group = savedSchedule.group
            Glide.with(binding.image)
                .load(R.drawable.ic_group_placeholder)
                .into(binding.image)
            binding.scheduleTitle.text = group.name
            binding.scheduleDegree.text = group.faculty?.abbrev
            binding.scheduleRank.text = group.speciality?.abbrev
            binding.scheduleCourse.visibility = View.VISIBLE
            binding.scheduleCourse.text = "${group.course} $courseText"

            var facultyDescription = ""
            if (group.faculty != null) {
                facultyDescription = "${group.faculty?.abbrev} - ${group.faculty?.name}\n\n"
            }
            if (group.speciality != null) {
                facultyDescription += "${group.speciality?.abbrev} - ${group.speciality?.name}"
            }
            binding.scheduleSubtitles.text = facultyDescription

            binding.lastUpdate.text = lastUpdateText
        } else {
            val employee = savedSchedule.employee
            Glide.with(binding.image)
                .load(employee.photoLink)
                .into(binding.image)
            binding.scheduleTitle.text = employee.getFullName()
            binding.scheduleDegree.text = employee.degreeFull
            binding.scheduleRank.text = employee.rank
            binding.scheduleSubtitles.text = employee.getFullDepartments("\n\n")
            binding.lastUpdate.text = lastUpdateText
        }

        binding.deleteButton.setOnClickListener {
            delete(savedSchedule)
            dismiss()
        }

        binding.updateButton.setOnClickListener {
            update(savedSchedule)
            dismiss()
        }

        return binding.root
    }

}


