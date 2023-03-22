package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSubjectAddBinding
import com.bsuir.bsuirschedule.presentation.viewModels.ScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleSubjectAddFragment : Fragment() {

    private val scheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleSubjectAddBinding.inflate(inflater)

        scheduleVM.scheduleStatus.observe(viewLifecycleOwner) { schedule ->
            if (schedule == null) return@observe

            if (schedule.isGroup()) {
                binding.groupEditText.visibility = View.VISIBLE
                binding.employeeEditText.visibility = View.GONE
            } else {
                binding.groupEditText.visibility = View.GONE
                binding.employeeEditText.visibility = View.VISIBLE
            }
        }

        return binding.root
    }
}