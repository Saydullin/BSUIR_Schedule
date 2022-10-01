package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentFirstScheduleAddBinding
import com.bsuir.bsuirschedule.presentation.viewModels.GroupScheduleViewModel
import com.bsuir.bsuirschedule.presentation.viewModels.SavedSchedulesViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class FirstScheduleAddFragment : Fragment() {

    private val groupScheduleVM: GroupScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFirstScheduleAddBinding.inflate(inflater)

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_firstScheduleAddFragment_to_mainScheduleFragment)
        }

        binding.doneButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_firstScheduleAddFragment_to_mainScheduleFragment)
        }

        groupScheduleVM.scheduleLoadedStatus.observe(viewLifecycleOwner) { savedSchedule ->
            if (savedSchedule == null) return@observe
            binding.doneButton.alpha = 1f
        }

        return binding.root
    }

}