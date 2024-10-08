package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSubjectEditBinding
import com.bsuir.bsuirschedule.domain.models.ChangeSubjectSettings
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleSubjectEdit
import com.bsuir.bsuirschedule.domain.models.ScheduleTerm
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
        var scheduleTerm: ScheduleTerm = ScheduleTerm.NOTHING

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectEditFragment_to_mainScheduleFragment)
        }

        groupScheduleVM.activeSubjectStatus.observe(viewLifecycleOwner) { activeSubject ->
            val schedule = groupScheduleVM.getActiveSchedule() ?: return@observe
            scheduleTerm = schedule.settings.term.selectedTerm
            binding.customSubjectView.setGroupType(!schedule.isGroup())
            binding.customSubjectView.setSubgroups(schedule.subgroups)
            binding.customSubjectView.setSelectedSubgroup(activeSubject.getEditedOrNumSubgroup())
            binding.customSubjectView.setSubjectType(activeSubject.lessonTypeAbbrev ?: ScheduleSubject.LESSON_TYPE_LECTURE)
            binding.customSubjectView.setStartTime(activeSubject.startLessonTime ?: "00:00")
            binding.customSubjectView.setEndTime(activeSubject.endLessonTime ?: "00:00")
            binding.customSubjectView.setTotalHours(activeSubject.hours?.total ?: 0)
            binding.customSubjectView.setShortTitle(activeSubject.getEditedOrShortTitle())
            binding.customSubjectView.setFullTitle(activeSubject.getEditedOrFullTitle())
            binding.customSubjectView.setNote(activeSubject.getEditedOrNote())
            binding.customSubjectView.setAudience(activeSubject.getEditedOrAudienceInLine())
            binding.customSubjectView.setWeeks(activeSubject.weekNumber ?: arrayListOf())
            binding.customSubjectView.setWeekDay(activeSubject.dayNumber)
            if (schedule.isGroup()) {
                val dtoText = activeSubject.employees?.joinToString(", ") { it.getName() } ?: ""
                binding.customSubjectView.setSourceScheduleItem(dtoText)
            } else {
                val dtoText = activeSubject.groups?.joinToString(", ") { it.name } ?: ""
                binding.customSubjectView.setSourceScheduleItem(dtoText)
            }

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
        }

        val onSourceSelect = { savedSchedule: SavedSchedule ->
            binding.customSubjectView.setSourceScheduleItem(savedSchedule.getName())
        }

        binding.customSubjectView.setOnCancelListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectEditFragment_to_mainScheduleFragment)
        }

        binding.customSubjectView.setOnAddSourceScheduleListener { isGroup ->
            val addScheduleItemDialog = AddScheduleItemDialog(isGroup, onSourceSelect)
            addScheduleItemDialog.isCancelable = true
            addScheduleItemDialog.show(parentFragmentManager, "AddSourceScheduleListener")
        }

        binding.customSubjectView.setOnSelectScheduleListener { subject, sourceItemsText ->
            val activeSubject = groupScheduleVM.getActiveSubject() ?: return@setOnSelectScheduleListener
            // sourceItemsText get from vm from useCase
            val subjectEdit = ScheduleSubjectEdit(
                shortTitle = subject.getEditedOrShortTitle(),
                fullTitle = subject.getEditedOrFullTitle(),
                audience = subject.getEditedOrAudienceInLine(),
                startTime = subject.startLessonTime ?: "",
                endTime = subject.endLessonTime ?: "",
                weekDay = subject.dayNumber,
                lessonType = subject.lessonTypeAbbrev ?: "",
                weeks = subject.weekNumber ?: arrayListOf(),
                note = subject.note ?: "",
                subgroup = subject.numSubgroup ?: 0,
                sourceItems = arrayListOf()
            )
            activeSubject.edited = subjectEdit
            val changeSubjectSettings = ChangeSubjectSettings(
                forAll = true,
                forOnlyType = false,
                forOnlyPeriod = false,
                forOnlySubgroup = false
            )
            groupScheduleVM.editSubject(
                activeSubject,
                changeSubjectSettings,
                scheduleTerm
            )
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectEditFragment_to_mainScheduleFragment)
        }

        return binding.root
    }

}


