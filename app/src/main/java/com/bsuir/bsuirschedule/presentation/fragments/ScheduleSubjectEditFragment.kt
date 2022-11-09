package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectEditFragment_to_mainScheduleFragment)
        }

        binding.cancel.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_scheduleSubjectEditFragment_to_mainScheduleFragment)
        }

        groupScheduleVM.activeSubjectStatus.observe(viewLifecycleOwner) { activeSubject ->
            binding.shortNameEditText.setText(activeSubject.subject)
            binding.fullNameEditText.setText(activeSubject.subjectFullName)
            binding.noteEditText.setText(activeSubject.note)
            binding.audienceEditText.setText(activeSubject.getAudienceInLine())
            binding.lessonTypeEditText.setText(activeSubject.lessonTypeAbbrev)
            binding.weeksEditText.setText(activeSubject.weekNumber?.joinToString(", ") ?: "")
        }

        binding.saveButton.setOnClickListener {
            val actualSubjectVM = groupScheduleVM.activeSubjectStatus.value ?: return@setOnClickListener

            val actualSubject = actualSubjectVM.copy()

            actualSubject.subject = binding.shortNameEditText.text.toString()
            actualSubject.subjectFullName = binding.fullNameEditText.text.toString()
//            if (binding.audienceEditText.text.toString().trim().isNotEmpty()) {
//                actualSubject.audience = binding.audienceEditText.text.toString().split(", ") as ArrayList<String>
//            }
            actualSubject.note = binding.noteEditText.text.toString()

            groupScheduleVM.setActiveSubject(actualSubject)
        }

        return binding.root
    }

}


