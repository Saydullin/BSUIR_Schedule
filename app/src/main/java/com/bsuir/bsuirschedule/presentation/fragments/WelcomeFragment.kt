package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentWelcomeBinding
import com.bsuir.bsuirschedule.domain.models.WelcomeText
import com.bsuir.bsuirschedule.presentation.adapters.WelcomeAdapter

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWelcomeBinding.inflate(inflater)

        val welcomeTextList = ArrayList<WelcomeText>()
        welcomeTextList.add(
            WelcomeText(
                title = getString(R.string.welcome_title_1),
                caption = getString(R.string.welcome_caption_1)
            )
        )
        welcomeTextList.add(
            WelcomeText(
                title = getString(R.string.welcome_title_2),
                caption = getString(R.string.welcome_caption_2)
            )
        )
        welcomeTextList.add(
            WelcomeText(
                title = getString(R.string.welcome_title_3),
                caption = getString(R.string.welcome_caption_3)
            )
        )
        welcomeTextList.add(
            WelcomeText(
                title = getString(R.string.welcome_title_4),
                caption = getString(R.string.welcome_caption_4)
            )
        )
        welcomeTextList.add(
            WelcomeText(
                title = getString(R.string.welcome_title_5),
                caption = getString(R.string.welcome_caption_5)
            )
        )
        welcomeTextList.add(
            WelcomeText(
                title = getString(R.string.welcome_title_6),
                caption = getString(R.string.welcome_caption_6)
            )
        )

        val onGetStartedClick = {
            Navigation.findNavController(binding.root).navigate(R.id.action_welcomeFragment_to_scheduleItemsFragment)
            Toast.makeText(context, "Загрузите любое расписание", Toast.LENGTH_SHORT).show()
        }

        binding.viewPager.adapter = WelcomeAdapter(context!!, welcomeTextList, onGetStartedClick)

        return binding.root
    }

}