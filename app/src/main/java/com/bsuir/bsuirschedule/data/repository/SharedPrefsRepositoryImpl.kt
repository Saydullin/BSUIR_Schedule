package com.bsuir.bsuirschedule.data.repository

import android.content.Context
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.repository.SharedPrefsRepository

class ThemeType {
    companion object {
        const val SYSTEM = 0
        const val DARK = 1
        const val LIGHT = 2
    }
}

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

    override fun getPrevVersion(): Int {
        return prefs.getInt(
            context.getString(R.string.app_prev_version),
            0
        )
    }

    override fun setPrevVersion(prevVersion: Int) {
        with(prefs.edit()) {
            putInt(
                context.getString(R.string.app_prev_version),
                prevVersion
            )
            apply()
        }
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

    override fun getThemeType(): Int {
        return prefs.getInt(
            context.getString(R.string.active_theme_code),
            0
        )
    }

    override fun setTheme(themeType: Int) {
        with(prefs.edit()) {
            putInt(
                context.getString(R.string.active_theme_code),
                themeType
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

    override fun isNotificationsEnabled(): Boolean {
        return prefs.getBoolean(
            context.getString(R.string.preference_is_notify_enabled),
            true
        )
    }

    override fun setNotificationsEnabled(isNotificationsEnabled: Boolean) {
        with(prefs.edit()) {
            putBoolean(
                context.getString(R.string.preference_is_notify_enabled),
                isNotificationsEnabled
            )
            apply()
        }
    }

    override fun getDefaultScheduleTitle(): String? {
        return prefs.getString(
            context.getString(R.string.preference_default_schedule_title),
            null
        )
    }

    override fun setDefaultScheduleTitle(scheduleTitle: String) {
        with(prefs.edit()) {
            putString(
                context.getString(R.string.preference_default_schedule_title),
                scheduleTitle
            )
            apply()
        }
    }

}


