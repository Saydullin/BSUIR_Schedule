package com.bsuir.bsuirschedule.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.DeleteSubjectDialogBinding
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject

class DeleteSubjectDialog(
    private val scheduleSubject: ScheduleSubject,
    private val agreeCallback: (scheduleSubject: ScheduleSubject) -> Unit,
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DeleteSubjectDialogBinding.inflate(inflater)

        isCancelable = true
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        val deleteText = resources.getString(R.string.delete_subject, scheduleSubject.subjectFullName)

        binding.title.text = deleteText

        binding.agreeButton.setOnClickListener {
            agreeCallback(scheduleSubject)
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}

