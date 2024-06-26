package com.bsuir.bsuirschedule.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.DeleteSubjectDialogBinding
import com.bsuir.bsuirschedule.domain.models.ChangeSubjectSettings
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.presentation.utils.SubjectManager

class DeleteSubjectDialog(
    private val scheduleSubject: ScheduleSubject,
    private val agreeCallback: (scheduleSubject: ScheduleSubject, deleteSettings: ChangeSubjectSettings) -> Unit,
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DeleteSubjectDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_bg_shadow)

        isCancelable = true
        val deleteText = resources.getString(R.string.delete_subject, scheduleSubject.getEditedOrShortTitle())
        val subjectManager = SubjectManager(subject = scheduleSubject, context = context!!)

        binding.title.text = deleteText

        val deleteAllSubjects = getString(R.string.delete_subject_dialog_all, scheduleSubject.getEditedOrShortTitle())
        val deleteTypeSubjects = getString(
            R.string.delete_subject_dialog_type,
            subjectManager.getSubjectType(scheduleSubject.getEditedOrLessonType()),
            scheduleSubject.getEditedOrShortTitle(),
        )
        val deletePeriodSubjects = getString(
            R.string.delete_subject_dialog_period,
            scheduleSubject.getEditedOrShortTitle(),
            subjectManager.getDayOfWeek(),
            subjectManager.getSubjectWeeks()
        )

        if (scheduleSubject.getEditedOrNumSubgroup() != 0) {
            val deleteSubgroupSubjects = getString(
                R.string.delete_subject_dialog_subgroup,
                scheduleSubject.getEditedOrNumSubgroup()
            )
            binding.deleteAllSubjectsSubgroup.text = deleteSubgroupSubjects
        } else {
            binding.deleteAllSubjectsSubgroup.visibility = View.GONE
        }

        binding.deleteAllSubjects.text = deleteAllSubjects
        binding.deleteAllSubjectsType.text = deleteTypeSubjects
        binding.deleteAllSubjectsPeriod.text = deletePeriodSubjects

        binding.agreeButton.setOnClickListener {
            val deleteSettings = ChangeSubjectSettings(
                forAll = binding.deleteAllSubjects.isChecked,
                forOnlyType = binding.deleteAllSubjectsType.isChecked,
                forOnlyPeriod = binding.deleteAllSubjectsPeriod.isChecked,
                forOnlySubgroup = binding.deleteAllSubjectsSubgroup.isChecked
            )
            agreeCallback(scheduleSubject, deleteSettings)
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}

