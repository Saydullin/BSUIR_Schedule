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

    override fun setActiveScheduleId(scheduleId: Int) {
        with(prefs.edit()) {
            putInt(
                context.getString(R.string.active_schedule_id),
                scheduleId
            )
            apply()
        }
    }

}


