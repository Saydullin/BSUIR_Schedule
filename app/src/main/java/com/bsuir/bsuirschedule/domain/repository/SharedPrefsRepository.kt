package com.bsuir.bsuirschedule.domain.repository

import android.content.Context

interface SharedPrefsRepository {

    val context: Context

    fun isFirstTime(): Boolean

    fun setFirstTime(isFirst: Boolean)

    fun getActiveScheduleId(): Int

    fun isAutoUpdate(): Boolean

    fun setAutoUpdate(isAutoUpdate: Boolean)

    fun setActiveScheduleId(scheduleId: Int)

    fun getLanguage(): String?

    fun setLanguage(lang: String)

    fun getThemeIsDark(): Boolean

    fun setTheme(isDark: Boolean)

    fun getScheduleUpdateCounter(): Int

    fun setScheduleUpdateCounter(counter: Int)

    fun getScheduleAutoUpdateDate(): String

    fun setScheduleAutoUpdateDate(autoUpdateDate: String)

    fun isNotificationsEnabled(): Boolean

    fun setNotificationsEnabled(isNotificationsEnabled: Boolean)

    fun getDefaultScheduleTitle(): String?

    fun setDefaultScheduleTitle(scheduleTitle: String)

}


