package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAboutBinding.inflate(inflater)

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_aboutFragment_to_settingsFragment)
        }

        return binding.root
    }

}