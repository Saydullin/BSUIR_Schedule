package com.bsuir.bsuirschedule.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.navigation.Navigation
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.data.repository.SharedPrefsRepositoryImpl
import com.bsuir.bsuirschedule.databinding.FragmentSettingsBinding
import com.bsuir.bsuirschedule.databinding.SettingsFontBinding
import com.bsuir.bsuirschedule.databinding.SettingsLangBinding
import com.bsuir.bsuirschedule.databinding.SettingsThemeBinding
import java.util.*

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onResume() {
        super.onResume()

        val prefs = SharedPrefsRepositoryImpl(requireContext())
        setLangUI(binding.nestedLangSettings, prefs.getLanguage())
        setFontSizeUI(binding.nestedFontSettings)
        setThemeUI(binding.nestedThemeSettings, prefs.getThemeIsDark())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        val prefs = SharedPrefsRepositoryImpl(requireContext())

        setLangUI(binding.nestedLangSettings, prefs.getLanguage())
        setFontSizeUI(binding.nestedFontSettings)
        setThemeUI(binding.nestedThemeSettings, prefs.getThemeIsDark())

        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_settingsFragment_to_mainScheduleFragment)
        }

        val locales = AppCompatDelegate.getApplicationLocales()

        Log.e("sady", locales.toLanguageTags())

        binding.nestedLangSettings.autoCompleteLangTextView.setOnItemClickListener { _, _, i, _ ->
            when (getLangList()[i].lowercase()) {
                resources.getString(R.string.settings_lang_en).lowercase() -> {
                    Locale.setDefault(Locale("en-EN"))
                    prefs.setLanguage("en-EN")
                    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("en-EN")
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
                resources.getString(R.string.settings_lang_ru).lowercase() -> {
                    Locale.setDefault(Locale("ru-RU"))
                    prefs.setLanguage("ru-RU")
                    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("ru-RU")
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
                resources.getString(R.string.settings_lang_be).lowercase() -> {
                    Locale.setDefault(Locale("be-BE"))
                    prefs.setLanguage("be-BE")
                    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("be-BE")
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
                resources.getString(R.string.settings_lang_zh).lowercase() -> {
                    Locale.setDefault(Locale("zh"))
                    prefs.setLanguage("zh-CN")
                    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("zh")
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
            }
            val chosenLangText = resources.getString(R.string.settings_lang_chosen, getLangList()[i])
            Toast.makeText(context, chosenLangText, Toast.LENGTH_SHORT).show()
        }

        binding.nestedThemeSettings.autoCompleteThemeTextView.setOnItemClickListener { _, _, i, _ ->
            when (getThemeList()[i].lowercase()) {
                resources.getString(R.string.settings_theme_light).lowercase() -> {
                    prefs.setTheme(isDark = false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                resources.getString(R.string.settings_theme_dark).lowercase() -> {
                    prefs.setTheme(isDark = true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
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

    private fun setLangUI(langBinding: SettingsLangBinding, langCode: String?) {
        val langList = resources.getStringArray(R.array.languages)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, langList)
        langBinding.autoCompleteLangTextView.setAdapter(arrayAdapter)
        when (langCode) {
            resources.getString(R.string.settings_lang_en_code) -> {
                langBinding.autoCompleteLangTextView.setText(getString(R.string.settings_lang_en), false)
            }
            resources.getString(R.string.settings_lang_ru_code) -> {
                langBinding.autoCompleteLangTextView.setText(getString(R.string.settings_lang_ru), false)
            }
            resources.getString(R.string.settings_lang_be_code) -> {
                langBinding.autoCompleteLangTextView.setText(getString(R.string.settings_lang_be), false)
            }
            resources.getString(R.string.settings_lang_zh_code) -> {
                langBinding.autoCompleteLangTextView.setText(getString(R.string.settings_lang_zh), false)
            }
            null -> {
                langBinding.autoCompleteLangTextView.setText(getString(R.string.current_lang), false)
            }
        }
    }

    private fun setFontSizeUI(fontSizeBinding: SettingsFontBinding) {
        val fontSizesList = resources.getStringArray(R.array.font_size)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, fontSizesList)
        fontSizeBinding.autoCompleteFontTextView.setAdapter(arrayAdapter)
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


