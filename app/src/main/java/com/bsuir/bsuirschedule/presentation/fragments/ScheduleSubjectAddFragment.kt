package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bsuir.bsuirschedule.databinding.FragmentScheduleSubjectAddBinding

class ScheduleSubjectAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleSubjectAddBinding.inflate(inflater)



        return binding.root
    }
}