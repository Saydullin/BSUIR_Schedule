package com.bsuir.bsuirschedule.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.RenameScheduleDialogBinding
import com.bsuir.bsuirschedule.domain.models.SavedSchedule

class RenameScheduleDialog(
    private val savedSchedule: SavedSchedule,
    private val onRenameSubmit: (savedSchedule: SavedSchedule, newTitle: String) -> Unit
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = RenameScheduleDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)

        if (savedSchedule.isGroup) {
            if (savedSchedule.group.title != null) {
                binding.editText.setText(savedSchedule.group.getGroupTitle())
                binding.editText.setSelection(savedSchedule.group.title!!.length)
            }
        } else {
            if (savedSchedule.employee.title != null) {
                binding.editText.setText(savedSchedule.employee.getEmployeeTitle())
                binding.editText.setSelection(savedSchedule.employee.title!!.length)
            }
        }

        binding.submitButton.setOnClickListener {
            val newTitle = binding.editText.getText()
            onRenameSubmit(savedSchedule, newTitle)
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}