package com.bsuir.bsuirschedule.presentation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.data.repository.SharedPrefsRepositoryImpl
import com.bsuir.bsuirschedule.databinding.FragmentSettingsBinding
import com.bsuir.bsuirschedule.databinding.SettingsFontBinding
import com.bsuir.bsuirschedule.databinding.SettingsLangBinding
import com.bsuir.bsuirschedule.databinding.SettingsThemeBinding
import com.bsuir.bsuirschedule.presentation.activities.MainActivity

class SettingsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSettingsBinding.inflate(inflater)
        val prefs = SharedPrefsRepositoryImpl(requireContext())

        setLangUI(binding.nestedLangSettings, prefs.getLanguage())
        setFontSizeUI(binding.nestedFontSettings)
        setThemeUI(binding.nestedThemeSettings, prefs.getThemeIsDark())

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_settingsFragment_to_mainScheduleFragment)
        }

        binding.nestedLangSettings.autoCompleteTextView.setOnItemClickListener { _, _, i, _ ->
            when (getLangList()[i].lowercase()) {
                resources.getString(R.string.settings_lang_en).lowercase() -> {
                    prefs.setLanguage("en")
                }
                resources.getString(R.string.settings_lang_ru).lowercase() -> {
                    prefs.setLanguage("ru")
                }
                resources.getString(R.string.settings_lang_be).lowercase() -> {
                    prefs.setLanguage("be")
                }
                resources.getString(R.string.settings_lang_zh).lowercase() -> {
                    prefs.setLanguage("zh")
                }
            }
            requireActivity().run{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            val chosenLangText = resources.getString(R.string.settings_lang_chosen, getLangList()[i])
            Toast.makeText(context, chosenLangText, Toast.LENGTH_SHORT).show()
        }

        binding.nestedThemeSettings.autoCompleteTextView.setOnItemClickListener { _, _, i, _ ->
            when (getThemeList()[i].lowercase()) {
                resources.getString(R.string.settings_theme_light).lowercase() -> {
                    prefs.setTheme(isDark = false)
                }
                resources.getString(R.string.settings_theme_dark).lowercase() -> {
                    prefs.setTheme(isDark = true)
                }
            }

            requireActivity().run{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            val chosenThemeText = resources.getString(R.string.settings_theme_chosen, getThemeList()[i])
            Toast.makeText(context, chosenThemeText, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun getLangList(): Array<out String> {
        return resources.getStringArray(R.array.languages)
    }

    private fun getThemeList(): Array<out String> {
        return resources.getStringArray(R.array.app_theme)
    }

    private fun setLangUI(langBinding: SettingsLangBinding, langCode: String) {
        val langList = resources.getStringArray(R.array.languages)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, langList)
        langBinding.autoCompleteTextView.setAdapter(arrayAdapter)
        when (langCode) {
            resources.getString(R.string.settings_lang_en_code) -> {
                langBinding.autoCompleteTextView.setText(getString(R.string.settings_lang_en), false)
            }
            resources.getString(R.string.settings_lang_ru_code) -> {
                langBinding.autoCompleteTextView.setText(getString(R.string.settings_lang_ru), false)
            }
            resources.getString(R.string.settings_lang_be_code) -> {
                langBinding.autoCompleteTextView.setText(getString(R.string.settings_lang_be), false)
            }
            resources.getString(R.string.settings_lang_zh_code) -> {
                langBinding.autoCompleteTextView.setText(getString(R.string.settings_lang_zh), false)
            }
        }
    }

    private fun setFontSizeUI(fontSizeBinding: SettingsFontBinding) {
        val fontSizesList = resources.getStringArray(R.array.font_size)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, fontSizesList)
        fontSizeBinding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    private fun setThemeUI(themeBinding: SettingsThemeBinding, isDarkTheme: Boolean) {
        val themesList = resources.getStringArray(R.array.app_theme)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, themesList)
        themeBinding.autoCompleteTextView.setAdapter(arrayAdapter)
        if (isDarkTheme) {
            themeBinding.autoCompleteTextView.setText(getString(R.string.settings_theme_dark), false)
        } else {
            themeBinding.autoCompleteTextView.setText(getString(R.string.settings_theme_light), false)
        }
    }

}


