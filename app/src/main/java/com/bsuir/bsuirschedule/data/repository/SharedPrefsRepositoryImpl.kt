package com.bsuir.bsuirschedule.data.repository

import android.content.Context
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.repository.SharedPrefsRepository

class SharedPrefsRepositoryImpl(override val context: Context): SharedPrefsRepository {

    private val fileName = "BSUIRSchedulePrefs"
    private val prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    override fun isFirstTime(): Boolean {
        return prefs.getBoolean(
            context.getString(R.string.preference_is_first_time),
            true
        )
    }

    override fun setFirstTime(isFirst: Boolean) {
        with(prefs.edit()) {
            putBoolean(
                context.getString(R.string.preference_is_first_time),
                isFirst
            )
            apply()
        }
    }

    override fun getActiveScheduleId(): Int {
        return prefs.getInt(
            context.getString(R.string.active_schedule_id),
            -1
        )
    }

    override fun isAutoUpdate(): Boolean {
        return prefs.getBoolean(
            context.getString(R.string.auto_update_schedule_id),
            false
        )
    }

    override fun setAutoUpdate(isAutoUpdate: Boolean) {
        with(prefs.edit()) {
            putBoolean(
                context.getString(R.string.auto_update_schedule_id),
                isAutoUpdate
            )
            apply()
        }
    }

    override fun setActiveScheduleId(scheduleId: Int) {
        with(prefs.edit()) {
            putInt(
                context.getString(R.string.active_schedule_id),
                scheduleId
            )
            apply()
        }
    }

    override fun getLanguage(): String? {
        return prefs.getString(
            context.getString(R.string.active_language_code), null
        )
    }

    override fun setLanguage(lang: String) {
        with(prefs.edit()) {
            putString(
                context.getString(R.string.active_language_code),
                lang
            )
            apply()
        }
    }

    override fun getThemeIsDark(): Boolean {
        return prefs.getBoolean(
            context.getString(R.string.active_theme_code),
            false
        )
    }

    override fun setTheme(isDark: Boolean) {
        with(prefs.edit()) {
            putBoolean(
                context.getString(R.string.active_theme_code),
                isDark
            )
            apply()
        }
    }

    override fun getScheduleUpdateCounter(): Int {
        return prefs.getInt(
            context.getString(R.string.schedule_update_counter),
            -1
        )
    }

    override fun setScheduleUpdateCounter(counter: Int) {
        with(prefs.edit()) {
            putInt(
                context.getString(R.string.schedule_update_counter),
                counter
            )
            apply()
        }
    }

    override fun getScheduleAutoUpdateDate(): String {
        return prefs.getString(
            context.getString(R.string.schedule_auto_update_date), ""
        ) ?: ""
    }

    override fun setScheduleAutoUpdateDate(autoUpdateDate: String) {
        with(prefs.edit()) {
            putString(
                context.getString(R.string.schedule_auto_update_date),
                autoUpdateDate
            )
            apply()
        }
    }

}


