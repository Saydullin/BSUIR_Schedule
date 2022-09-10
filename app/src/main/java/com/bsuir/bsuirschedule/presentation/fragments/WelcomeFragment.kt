package com.bsuir.bsuirschedule.presentation.fragments

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentWelcomeBinding
import com.bsuir.bsuirschedule.domain.models.WelcomeText
import com.bsuir.bsuirschedule.presentation.adapters.WelcomeAdapter
import com.google.android.material.tabs.TabLayoutMediator

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWelcomeBinding.inflate(inflater)
        var prevPosition = 0

        val welcomeTextList = ArrayList<WelcomeText>()
        welcomeTextList.add(
            WelcomeText(
                title = getString(R.string.welcome_title_bsuir_schedule),
                caption = getString(R.string.welcome_caption_bsuir_schedule),
                image = resources.getDrawable(R.drawable.ic_logo_icon_primary, null)
            )
        )
        welcomeTextList.add(
            WelcomeText(
                title = getString(R.string.welcome_title_search),
                caption = getString(R.string.welcome_caption_search),
                image = resources.getDrawable(R.drawable.ic_search_primary, null)
            )
        )
        welcomeTextList.add(
            WelcomeText(
                title = getString(R.string.welcome_title_info),
                caption = getString(R.string.welcome_caption_info),
                image = resources.getDrawable(R.drawable.ic_info_primary, null)
            )
        )
        welcomeTextList.add(
            WelcomeText(
                title = getString(R.string.welcome_title_functionality),
                caption = getString(R.string.welcome_caption_functionality),
                image = resources.getDrawable(R.drawable.ic_settings_primary, null)
            )
        )
        welcomeTextList.add(
            WelcomeText(
                title = getString(R.string.welcome_title_localization),
                caption = getString(R.string.welcome_caption_localization),
                image = resources.getDrawable(R.drawable.ic_language_primary, null)
            )
        )
        welcomeTextList.add(
            WelcomeText(
                title = getString(R.string.welcome_title_etc),
                caption = getString(R.string.welcome_caption_etc),
                image = resources.getDrawable(R.drawable.ic_tune_primary, null)
            )
        )

        binding.getStartedButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_welcomeFragment_to_mainScheduleFragment)
            Toast.makeText(context, "Загрузите любое расписание", Toast.LENGTH_SHORT).show()
        }

        binding.viewPager.adapter = WelcomeAdapter(context!!, welcomeTextList)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.view.background = resources.getDrawable(R.drawable.dot, null)
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val tab = binding.tabLayout.getTabAt(position)
                val prevTab = binding.tabLayout.getTabAt(prevPosition)
                if (prevTab != null) {
                    prevTab.view.background = resources.getDrawable(R.drawable.dot, null)
                }
                if (tab != null) {
                    tab.view.background = resources.getDrawable(R.drawable.dot_selected, null)
                }
                prevPosition = position
                if (position == welcomeTextList.size-1) {
                    binding.getStartedButton.visibility = View.VISIBLE
                    binding.getStartedButton.alpha = 0f
                    binding.getStartedButton.animate().setStartDelay(300).alpha(1f)
                }
            }
        })

        return binding.root
    }

}