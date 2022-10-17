package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.FragmentSettingsBinding
import com.bsuir.bsuirschedule.databinding.SettingsFontBinding
import com.bsuir.bsuirschedule.databinding.SettingsLangBinding
import com.bsuir.bsuirschedule.databinding.SettingsThemeBinding
import java.util.*

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSettingsBinding.inflate(inflater)

        setLangUI(binding.nestedLangSettings)
        setFontSizeUI(binding.nestedFontSettings)
        setThemeUI(binding.nestedThemeSettings)

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_settingsFragment_to_mainScheduleFragment)
        }

        binding.nestedLangSettings.autoCompleteTextView.setOnItemClickListener { _, _, i, _ ->
            val metrics = resources.displayMetrics
            val configuration = resources.configuration
            when (getLangList()[i].lowercase()) {
                resources.getString(R.string.settings_lang_en).lowercase() -> {
                    configuration.setLocale(Locale.ENGLISH)
                }
                resources.getString(R.string.settings_lang_ru).lowercase() -> {
                    configuration.setLocale(Locale("ru"))
                }
                resources.getString(R.string.settings_lang_be).lowercase() -> {
                    configuration.setLocale(Locale("be"))
                }
            }
            resources.updateConfiguration(configuration, metrics)
            onConfigurationChanged(configuration)
            val chosenLangText = resources.getString(R.string.settings_lang_chosen, getLangList()[i])
            Toast.makeText(context, chosenLangText, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun getLangList(): Array<out String> {
        return resources.getStringArray(R.array.languages)
    }

    private fun setLangUI(langBinding: SettingsLangBinding) {
        val langList = resources.getStringArray(R.array.languages)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, langList)
        langBinding.autoCompleteTextView.setAdapter(arrayAdapter)
        // Set here default value from Shared Prefs
    }

    private fun setFontSizeUI(fontSizeBinding: SettingsFontBinding) {
        val fontSizesList = resources.getStringArray(R.array.font_size)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, fontSizesList)
        fontSizeBinding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    private fun setThemeUI(themeBinding: SettingsThemeBinding) {
        val themesList = resources.getStringArray(R.array.app_theme)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, themesList)
        themeBinding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

}


