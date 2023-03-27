package com.bsuir.bsuirschedule.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.data.repository.ThemeType
import com.bsuir.bsuirschedule.databinding.FragmentSettingsBinding
import com.bsuir.bsuirschedule.databinding.SettingsThemeBinding
import com.bsuir.bsuirschedule.domain.usecase.SharedPrefsUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class SettingsFragment : Fragment(), KoinComponent {

    private val googlePlayLink = "https://play.google.com/store/apps/details?id=com.bsuir.bsuirschedule&reviewId=0"
    private lateinit var binding: FragmentSettingsBinding
    private val sharedPrefsUseCase: SharedPrefsUseCase by inject()

    override fun onResume() {
        super.onResume()

        setThemeUI(binding.nestedThemeSettings, sharedPrefsUseCase.getThemeType())
        binding.notificationCheckbox.setChecked(sharedPrefsUseCase.isNotificationsEnabled())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_settingsFragment_to_mainScheduleFragment)
        }

        binding.questionButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_settingsFragment_to_aboutFragment)
        }

        binding.nestedThemeSettings.autoCompleteThemeTextView.setOnItemClickListener { _, _, i, _ ->
            when (getThemeList()[i].lowercase()) {
                resources.getString(R.string.settings_theme_light).lowercase() -> {
                    sharedPrefsUseCase.setThemeType(themeType = ThemeType.LIGHT)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                resources.getString(R.string.settings_theme_dark).lowercase() -> {
                    sharedPrefsUseCase.setThemeType(themeType = ThemeType.DARK)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                resources.getString(R.string.settings_theme_system).lowercase() -> {
                    sharedPrefsUseCase.setThemeType(themeType = ThemeType.SYSTEM)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }

            val chosenThemeText = resources.getString(R.string.settings_theme_chosen, getThemeList()[i])
            Toast.makeText(context, chosenThemeText, Toast.LENGTH_SHORT).show()
        }

        binding.notificationCheckbox.setOnCheckListener { isChecked ->
            val isNotificationsEnabled = sharedPrefsUseCase.isNotificationsEnabled()

            if (isNotificationsEnabled != isChecked) {
                val notificationsText = if (isChecked)
                    resources.getString(R.string.settings_notifications_enabled)
                else
                    resources.getString(R.string.settings_notifications_disabled)

                sharedPrefsUseCase.setNotificationsEnabled(isChecked)
                Toast.makeText(context, notificationsText, Toast.LENGTH_SHORT).show()
            }
        }

        binding.nestedRate.rateButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(googlePlayLink)))
        }

        return binding.root
    }

    private fun getThemeList(): Array<out String> {
        return resources.getStringArray(R.array.app_theme)
    }

    private fun setThemeUI(themeBinding: SettingsThemeBinding, themeType: Int) {
        val themesList = resources.getStringArray(R.array.app_theme)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, themesList)
        themeBinding.autoCompleteThemeTextView.setAdapter(arrayAdapter)
        when (themeType) {
            ThemeType.SYSTEM -> themeBinding.autoCompleteThemeTextView.setText(getString(R.string.settings_theme_system), false)
            ThemeType.DARK -> themeBinding.autoCompleteThemeTextView.setText(getString(R.string.settings_theme_dark), false)
            ThemeType.LIGHT -> themeBinding.autoCompleteThemeTextView.setText(getString(R.string.settings_theme_light), false)
        }
    }

}


