package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSubjectEditBinding
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.presentation.dialogs.AddScheduleItemDialog
import com.bsuir.bsuirschedule.presentation.viewModels.ScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleSubjectEditFragment : Fragment() {

    private val groupScheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleSubjectEditBinding.inflate(inflater)

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectEditFragment_to_mainScheduleFragment)
        }

        groupScheduleVM.activeSubjectStatus.observe(viewLifecycleOwner) { activeSubject ->
            val schedule = groupScheduleVM.getActiveSchedule() ?: return@observe
            binding.customSubjectView.setGroupType(!schedule.isGroup())
            binding.customSubjectView.setSubgroups(schedule.subgroups)
            binding.customSubjectView.setSelectedSubgroup(activeSubject.getNumSubgroup())
            binding.customSubjectView.setSubjectType(activeSubject.lessonTypeAbbrev ?: ScheduleSubject.LESSON_TYPE_LECTURE)
            binding.customSubjectView.setStartTime(activeSubject.startLessonTime ?: "00:00")
            binding.customSubjectView.setEndTime(activeSubject.endLessonTime ?: "00:00")
            binding.customSubjectView.setShortTitle(activeSubject.getShortTitle())
            binding.customSubjectView.setFullTitle(activeSubject.getFullTitle())
            binding.customSubjectView.setNote(activeSubject.getSubjectNote())
            binding.customSubjectView.setAudience(activeSubject.getAudienceInLine())
            binding.customSubjectView.setWeeks(activeSubject.weekNumber ?: arrayListOf())
            binding.customSubjectView.setWeekDay(activeSubject.dayNumber)

            if (activeSubject.employees != null) {
                val employees = activeSubject.employees!!
                if (employees.size > 0) {
                    binding.customSubjectView.setEmployee(employees.joinToString(", ") { it.getName() })
                }
            }
            if (activeSubject.groups != null) {
                val groups = activeSubject.groups!!
                if (groups.size > 0) {
                    binding.customSubjectView.setEmployee(groups.joinToString(", ") { it.name })
                }
            }

//            val deleteAllSubjects = getString(R.string.delete_subject_dialog_all, activeSubject.subject)
//            val deleteTypeSubjects = getString(
//                R.string.delete_subject_dialog_type,
//                subjectManager.getSubjectType(),
//                activeSubject.subject,
//            )
//            val deletePeriodSubjects = getString(
//                R.string.delete_subject_dialog_period,
//                activeSubject.subject,
//                subjectManager.getDayOfWeek(),
//                subjectManager.getSubjectWeeks()
//            )
//
//            binding.editSubjectsAll.text = deleteAllSubjects
//            binding.editSubjectsType.text = deleteTypeSubjects
//            binding.editSubjectsPeriod.text = deletePeriodSubjects
        }

        val onSourceSelect = { savedSchedule: SavedSchedule ->
            binding.customSubjectView.setSourceScheduleItem(savedSchedule.getName())
        }

        binding.customSubjectView.setOnAddSourceScheduleListener { isGroup ->
            val addScheduleItemDialog = AddScheduleItemDialog(isGroup, onSourceSelect)
            addScheduleItemDialog.isCancelable = true
            addScheduleItemDialog.show(parentFragmentManager, "AddSourceScheduleListener")
        }

//        binding.saveButton.setOnClickListener {
//            val subject = groupScheduleVM.getActiveSubject() ?: return@setOnClickListener
//
//            val editSubject = subject.copy()
//
//            editSubject.edited = ScheduleSubjectEdit(
//                shortTitle = binding.shortNameEditText.text.toString().trim(),
//                fullTitle = binding.fullNameEditText.text.toString().trim(),
//                audience = binding.audienceEditText.text.toString().trim(),
//                note = binding.noteEditText.text.toString().trim(),
//                subgroup = if (binding.nestedSubject.subjectSubgroup.text == allSubgroupsShortText) {
//                    0
//                } else {
//                    binding.nestedSubject.subjectSubgroup.text.toString().toInt()
//                }
//            )
//
//            val changeSubjectSettings = ChangeSubjectSettings(
//                forAll = binding.editSubjectsAll.isChecked,
//                forOnlyType = binding.editSubjectsType.isChecked,
//                forOnlyPeriod = binding.editSubjectsPeriod.isChecked,
//                forOnlySubgroup = binding.editSubjectsSubgroup.isChecked
//            )
//
//            groupScheduleVM.editSubject(editSubject, changeSubjectSettings)
//            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectEditFragment_to_mainScheduleFragment)
//        }
//
//        binding.resetButton.setOnClickListener {
//            val subject = groupScheduleVM.getActiveSubject() ?: return@setOnClickListener
//
//            val editSubject = subject.copy()
//
//            editSubject.edited = null
//
//            val changeSubjectSettings = ChangeSubjectSettings(
//                forAll = binding.editSubjectsAll.isChecked,
//                forOnlyType = binding.editSubjectsType.isChecked,
//                forOnlyPeriod = binding.editSubjectsPeriod.isChecked,
//                forOnlySubgroup = false
//            )
//
//            groupScheduleVM.editSubject(editSubject, changeSubjectSettings)
//            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectEditFragment_to_mainScheduleFragment)
//        }

        return binding.root
    }

}


