package com.bsuir.bsuirschedule.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ScheduleItemPreviewDialogBinding
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bumptech.glide.Glide

class ScheduleItemPreviewDialog(
    private val savedSchedule: SavedSchedule,
    private val downloadSchedule: (savedSchedule: SavedSchedule) -> Unit,
    private val showSchedule: (savedSchedule: SavedSchedule) -> Unit,
    private val isDownloaded: Boolean
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ScheduleItemPreviewDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_bg_shadow)

        if (savedSchedule.isGroup) {
            val group = savedSchedule.group
            Glide.with(binding.employeeImage)
                .load(R.drawable.ic_group_placeholder)
                .into(binding.employeeImage)
            binding.scheduleTitle.text = group.getTitleOrName()
            binding.scheduleDegreeAndRank.text = group.getFacultyAndSpecialityAbbr()
            binding.scheduleDepartments.text = "${group.course} ${getString(R.string.course)}"
        } else {
            val employee = savedSchedule.employee
            val degreeAndRank = employee.getDegreeAndRank()
            Glide.with(binding.employeeImage)
                .load(employee.photoLink)
                .placeholder(R.drawable.ic_person_placeholder)
                .into(binding.employeeImage)
            binding.scheduleTitle.text = employee.getFullName()
            if (degreeAndRank.isEmpty()) {
                binding.scheduleDegreeAndRank.visibility = View.GONE
            } else {
                binding.scheduleDegreeAndRank.text = employee.getDegreeAndRank()
            }
            binding.scheduleDepartments.text = employee.getShortDepartmentsAbbrList(", ")

            binding.employeeImage.setOnClickListener {
                val imageViewDialog = ImageViewDialog(
                    requireContext(),
                    employee.photoLink,
                    employee.getFullName(),
                )
                imageViewDialog.show()
                dismiss()
            }
        }

        if (isDownloaded) {
            binding.downloadButton.visibility = View.GONE
            binding.openSchedule.visibility = View.VISIBLE
        } else {
            binding.downloadButton.visibility = View.VISIBLE
            binding.openSchedule.visibility = View.GONE
        }

        binding.openSchedule.setOnClickListener {
            showSchedule(savedSchedule)
            dismiss()
        }

        binding.downloadButton.setOnClickListener {
            downloadSchedule(savedSchedule)
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}


