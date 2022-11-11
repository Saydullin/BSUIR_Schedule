package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSubjectEditBinding
import com.bsuir.bsuirschedule.domain.models.ChangeSubjectSettings
import com.bsuir.bsuirschedule.domain.models.ScheduleSubjectEdit
import com.bsuir.bsuirschedule.presentation.utils.SubjectManager
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleSubjectEditFragment : Fragment() {

    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleSubjectEditBinding.inflate(inflater)
        val allSubgroupsText = resources.getString(R.string.settings_all_subgroups)
        val allSubgroupsShortText = resources.getString(R.string.all_subgroups_short)

        binding.noteEditText.isVerticalScrollBarEnabled = true
        binding.nestedSubject.subgroupInfo.visibility = View.VISIBLE
        binding.nestedSubject.subjectBreakTime.visibility = View.GONE

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectEditFragment_to_mainScheduleFragment)
        }

        groupScheduleVM.activeSubjectStatus.observe(viewLifecycleOwner) { activeSubject ->
            val subjectManager = SubjectManager(activeSubject, requireContext())

            if (activeSubject.getNumSubgroup() != 0) {
                val deleteSubgroupSubjects = getString(
                    R.string.delete_subject_dialog_subgroup,
                    activeSubject.getNumSubgroup()
                )
                binding.editSubjectsSubgroup.text = deleteSubgroupSubjects
            } else {
                binding.editSubjectsSubgroup.visibility = View.GONE
            }

            subjectManager.setSubjectTypeView(binding.nestedSubject.subjectType)
            binding.nestedSubject.subjectStartLesson.text = activeSubject.startLessonTime ?: "--:--"
            binding.nestedSubject.subjectEndLesson.text = activeSubject.endLessonTime ?: "--:--"
            binding.shortNameEditText.setText(activeSubject.getShortTitle())
            binding.fullNameEditText.setText(activeSubject.getFullTitle())
            binding.noteEditText.setText(activeSubject.getSubjectNote())
            binding.audienceEditText.setText(activeSubject.getAudienceInLine())

            if (activeSubject.employees != null) {
                val employeeNotListedText = getString(R.string.no_teacher_title)
                if (activeSubject.employees!!.size > 0) {
                    binding.nestedSubject.subjectEmployeeName.text = activeSubject.employees!![0].getName()
                } else {
                    binding.nestedSubject.subjectEmployeeName.text = employeeNotListedText
                }
            }
            if (activeSubject.groups != null) {
                val groupNotListedText = getString(R.string.no_group_title)
                if (activeSubject.groups!!.size > 0) {
                    binding.nestedSubject.subjectEmployeeName.text = activeSubject.groups!![0].getTitleOrName()
                } else {
                    binding.nestedSubject.subjectEmployeeName.text = groupNotListedText
                }
            }

            if (activeSubject.numSubgroup == 0) {
                binding.nestedSubject.subjectSubgroup.text = resources.getString(R.string.all_subgroups_short)
            } else {
                binding.nestedSubject.subjectSubgroup.text = activeSubject.numSubgroup.toString()
            }

            val selectedSubgroup = activeSubject.numSubgroup
            val selectionText = if (selectedSubgroup == 0) {
                allSubgroupsText
            } else {
                resources.getString(R.string.settings_item_subgroup, selectedSubgroup)
            }
            binding.subgroupAutoCompleteTextView.setText(selectionText, false)

            val deleteAllSubjects = getString(R.string.delete_subject_dialog_all, activeSubject.subject)
            val deleteTypeSubjects = getString(
                R.string.delete_subject_dialog_type,
                subjectManager.getSubjectType(),
                activeSubject.subject,
            )
            val deletePeriodSubjects = getString(
                R.string.delete_subject_dialog_period,
                activeSubject.subject,
                subjectManager.getDayOfWeek(),
                subjectManager.getSubjectWeeks()
            )

            binding.editSubjectsAll.text = deleteAllSubjects
            binding.editSubjectsType.text = deleteTypeSubjects
            binding.editSubjectsPeriod.text = deletePeriodSubjects
        }

        groupScheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            val subgroups = ArrayList<String>()
            schedule.subgroups.forEach { subgroup ->
                if (subgroup == 0) {
                    subgroups.add(allSubgroupsText)
                } else {
                    subgroups.add(resources.getString(R.string.settings_item_subgroup, subgroup))
                }
            }
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, subgroups)
            binding.subgroupAutoCompleteTextView.setAdapter(arrayAdapter)
        }

        binding.shortNameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                binding.nestedSubject.subjectTitle.text = text.toString()
            }
        })

        binding.audienceEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                binding.nestedSubject.subjectAudience.text = text.toString()
            }
        })

        binding.noteEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                if ((text ?: "").trim().isNotEmpty()) {
                    binding.nestedSubject.subjectAdditional.visibility = View.VISIBLE
                    binding.nestedSubject.subjectNote.text = text.toString()
                } else {
                    binding.nestedSubject.subjectAdditional.visibility = View.GONE
                }
            }
        })

        binding.subgroupAutoCompleteTextView.setOnItemClickListener { _, _, i, _ ->
            val schedule = groupScheduleVM.getActiveSchedule() ?: return@setOnItemClickListener
            val selectedSubgroup = schedule.subgroups[i]
            binding.nestedSubject.subjectSubgroup.text = if (selectedSubgroup == 0) {
                allSubgroupsShortText
            } else {
                selectedSubgroup.toString()
            }
        }

        binding.saveButton.setOnClickListener {
            val subject = groupScheduleVM.getActiveSubject() ?: return@setOnClickListener

            val editSubject = subject.copy()

            editSubject.edited = ScheduleSubjectEdit(
                shortTitle = binding.shortNameEditText.text.toString().trim(),
                fullTitle = binding.fullNameEditText.text.toString().trim(),
                audience = binding.audienceEditText.text.toString().trim(),
                note = binding.noteEditText.text.toString().trim(),
                subgroup = if (binding.nestedSubject.subjectSubgroup.text == allSubgroupsShortText) {
                    0
                } else {
                    binding.nestedSubject.subjectSubgroup.text.toString().toInt()
                } // FIXME
            )

            val changeSubjectSettings = ChangeSubjectSettings(
                forAll = binding.editSubjectsAll.isChecked,
                forOnlyType = binding.editSubjectsType.isChecked,
                forOnlyPeriod = binding.editSubjectsPeriod.isChecked,
                forOnlySubgroup = binding.editSubjectsSubgroup.isChecked
            )

            groupScheduleVM.editSubject(editSubject, changeSubjectSettings)
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectEditFragment_to_mainScheduleFragment)
        }

        binding.resetButton.setOnClickListener {
            val subject = groupScheduleVM.getActiveSubject() ?: return@setOnClickListener

            val editSubject = subject.copy()

            editSubject.edited = null

            val changeSubjectSettings = ChangeSubjectSettings(
                forAll = binding.editSubjectsAll.isChecked,
                forOnlyType = binding.editSubjectsType.isChecked,
                forOnlyPeriod = binding.editSubjectsPeriod.isChecked,
                forOnlySubgroup = false
            )

            groupScheduleVM.editSubject(editSubject, changeSubjectSettings)
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectEditFragment_to_mainScheduleFragment)
        }

        return binding.root
    }

}


