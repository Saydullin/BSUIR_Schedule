package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentScheduleControlBinding
import com.bsuir.bsuirschedule.presentation.viewModels.ScheduleViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel

class ScheduleControlFragment : Fragment() {

    private val groupScheduleVM: ScheduleViewModel by koinNavGraphViewModel(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleControlBinding.inflate(inflater)

        groupScheduleVM.settingsUpdatedStatus.observe(viewLifecycleOwner) { isUpdated ->
            if (isUpdated) {
                val savedText = getString(R.string.save_schedule_settings)
                Toast.makeText(context, savedText, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

}


