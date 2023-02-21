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
import com.bsuir.bsuirschedule.databinding.FragmentSettingsBinding
import com.bsuir.bsuirschedule.databinding.SettingsNotificationBinding
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

        setThemeUI(binding.nestedThemeSettings, sharedPrefsUseCase.getThemeIsDark())
        setNotificationsUI(binding.nestedNotification, sharedPrefsUseCase.isNotificationsEnabled())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_settingsFragment_to_mainScheduleFragment)
        }

        binding.nestedThemeSettings.autoCompleteThemeTextView.setOnItemClickListener { _, _, i, _ ->
            when (getThemeList()[i].lowercase()) {
                resources.getString(R.string.settings_theme_light).lowercase() -> {
                    sharedPrefsUseCase.setThemeIsDark(isDark = false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                resources.getString(R.string.settings_theme_dark).lowercase() -> {
                    sharedPrefsUseCase.setThemeIsDark(isDark = true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }

            val chosenThemeText = resources.getString(R.string.settings_theme_chosen, getThemeList()[i])
            Toast.makeText(context, chosenThemeText, Toast.LENGTH_SHORT).show()
        }

        binding.nestedNotification.isNotificationsEnabled.setOnCheckedChangeListener { _, isChecked ->
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

    private fun setNotificationsUI(notificationBinding: SettingsNotificationBinding, isNotificationsEnabled: Boolean) {
        notificationBinding.isNotificationsEnabled.isChecked = isNotificationsEnabled
    }

    private fun setThemeUI(themeBinding: SettingsThemeBinding, isDarkTheme: Boolean) {
        val themesList = resources.getStringArray(R.array.app_theme)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, themesList)
        themeBinding.autoCompleteThemeTextView.setAdapter(arrayAdapter)
        if (isDarkTheme) {
            themeBinding.autoCompleteThemeTextView.setText(getString(R.string.settings_theme_dark), false)
        } else {
            themeBinding.autoCompleteThemeTextView.setText(getString(R.string.settings_theme_light), false)
        }
    }

}


