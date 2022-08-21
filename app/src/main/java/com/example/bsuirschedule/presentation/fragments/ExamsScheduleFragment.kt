package com.example.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.bsuirschedule.R
import com.example.bsuirschedule.databinding.FragmentExamsScheduleBinding
import com.example.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ExamsScheduleFragment : Fragment() {

    private val groupSchedule: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentExamsScheduleBinding.inflate(inflater)

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_fromExams_to_mainScheduleFragment)
        }

        return binding.root
    }

}


