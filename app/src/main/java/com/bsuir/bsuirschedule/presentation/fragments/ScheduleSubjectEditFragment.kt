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
        }

        val onSourceSelect = { savedSchedule: SavedSchedule ->
            binding.customSubjectView.setSourceScheduleItem(savedSchedule.getName())
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
                shortTitle = subject.subject ?: "",
                fullTitle = subject.getFullTitle(),
                audience = subject.getAudienceInLine(),
                startTime = subject.startLessonTime ?: "",
                endTime = subject.endLessonTime ?: "",
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
            groupScheduleVM.editSubject(activeSubject, changeSubjectSettings)
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectEditFragment_to_mainScheduleFragment)
        }

        return binding.root
    }

}


