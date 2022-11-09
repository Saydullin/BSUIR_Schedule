package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSubjectEditBinding
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

        binding.shortNameEditText.clearFocus()
        binding.fullNameEditText.clearFocus()
        binding.noteEditText.clearFocus()
        binding.audienceEditText.clearFocus()

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectEditFragment_to_mainScheduleFragment)
        }

        binding.cancel.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectEditFragment_to_mainScheduleFragment)
        }

        groupScheduleVM.activeSubjectStatus.observe(viewLifecycleOwner) { activeSubject ->
            binding.shortNameEditText.updateText(activeSubject.subject ?: "")
            binding.fullNameEditText.updateText(activeSubject.subjectFullName ?: "")
            binding.noteEditText.updateText(activeSubject.note ?: "")
            binding.audienceEditText.updateText(activeSubject.getAudienceInLine() ?: "")

            val selectedSubgroup = activeSubject.numSubgroup
            val selectionText = if (selectedSubgroup == 0) {
                allSubgroupsText
            } else {
                resources.getString(R.string.settings_item_subgroup, selectedSubgroup)
            }
            binding.subgroupAutoCompleteTextView.setText(selectionText, false)
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

            override fun afterTextChanged(p0: Editable?) {
                if (binding.shortNameEditText.hasFocus()) {
                    setSubjectShortTitle(p0.toString().trimStart())
                }
            }
        })

        binding.audienceEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.audienceEditText.hasFocus()) {
                    setSubjectAudience(p0.toString().trimStart())
                }
            }
        })

        binding.noteEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.noteEditText.hasFocus()) {
                    setSubjectNote(p0.toString().trimStart())
                }
            }
        })

        binding.subgroupAutoCompleteTextView.setOnItemClickListener { _, _, i, _ ->
            val schedule = groupScheduleVM.getActiveSchedule() ?: return@setOnItemClickListener
            val selectedSubgroup = schedule.subgroups[i]
            setSubjectSubgroup(selectedSubgroup)
        }

        binding.saveButton.setOnClickListener {
            setSubjectFullTitle(binding.fullNameEditText.text.toString().trim())
            // Call save activeSubject in Schedule
        }

        return binding.root
    }

    private fun EditText.updateText(text: String) {
        val focussed = hasFocus()
        if (focussed) {
            clearFocus()
        }
        setText(text)
        if (focussed) {
            requestFocus()
        }
        setSelection(text.length)
    }

    private fun setSubjectNote(note: String) {
        val actualSubjectVM = groupScheduleVM.activeSubjectStatus.value ?: return
        val actualSubject = actualSubjectVM.copy()
        actualSubject.note = note
        groupScheduleVM.setActiveSubject(actualSubject)
    }

    private fun setSubjectAudience(audience: String) {
        val actualSubjectVM = groupScheduleVM.activeSubjectStatus.value ?: return
        val actualSubject = actualSubjectVM.copy()
        actualSubject.audience = arrayListOf(audience)
        groupScheduleVM.setActiveSubject(actualSubject)
    }

    private fun setSubjectShortTitle(shortTitle: String) {
        val actualSubjectVM = groupScheduleVM.activeSubjectStatus.value ?: return
        val actualSubject = actualSubjectVM.copy()
        actualSubject.subject = shortTitle
        groupScheduleVM.setActiveSubject(actualSubject)
    }

    private fun setSubjectFullTitle(fullTitle: String) {
        val actualSubjectVM = groupScheduleVM.activeSubjectStatus.value ?: return
        val actualSubject = actualSubjectVM.copy()
        actualSubject.subjectFullName = fullTitle
        groupScheduleVM.setActiveSubject(actualSubject)
    }

    private fun setSubjectSubgroup(subgroup: Int) {
        val actualSubjectVM = groupScheduleVM.activeSubjectStatus.value ?: return
        val actualSubject = actualSubjectVM.copy()
        actualSubject.numSubgroup = subgroup
        groupScheduleVM.setActiveSubject(actualSubject)
    }

}


