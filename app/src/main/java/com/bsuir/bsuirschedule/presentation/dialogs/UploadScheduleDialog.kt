package com.bsuir.bsuirschedule.presentation.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.RenameScheduleDialogBinding
import com.bsuir.bsuirschedule.databinding.UploadScheduleDialogBinding
import com.bsuir.bsuirschedule.domain.models.SavedSchedule

class UploadScheduleDialog(
    private val savedSchedule: SavedSchedule,
    private val onUploadSubmit: (savedSchedule: SavedSchedule) -> Unit
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = UploadScheduleDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)

        val titleText = if (savedSchedule.isGroup) {
            getString(R.string.upload_schedule_dialog_title, savedSchedule.group.name)
        } else {
            getString(R.string.upload_schedule_dialog_title, savedSchedule.employee.getFullName())
        }
        binding.title.text = titleText

        binding.agreeButton.setOnClickListener {
            onUploadSubmit(savedSchedule)
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}