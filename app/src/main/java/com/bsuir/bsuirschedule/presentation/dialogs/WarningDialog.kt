package com.bsuir.bsuirschedule.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.WarningDialogBinding
import com.bsuir.bsuirschedule.domain.models.SavedSchedule

class WarningDialog(
    private val savedSchedule: SavedSchedule,
    private val agreeCallback: (savedSchedule: SavedSchedule) -> Unit,
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = WarningDialogBinding.inflate(inflater)

        isCancelable = true
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        val deleteText = if (savedSchedule.isGroup) {
            resources.getString(R.string.delete_schedule, savedSchedule.group.name)
        } else {
            resources.getString(R.string.delete_schedule, savedSchedule.employee.getFullName())
        }

        binding.title.text = deleteText

        binding.agreeButton.setOnClickListener {
            agreeCallback(savedSchedule)
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setRetainInstance(true)
        return super.onCreateDialog(savedInstanceState)
    }

}