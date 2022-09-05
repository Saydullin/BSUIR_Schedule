package com.bsuir.bsuirschedule.app.prefs

import android.app.Application
import android.content.Context
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.utils.Resource

class AppPreferencesImpl: Application() {

    private val fileName = "BSUIRSchedulePrefs"
    private val prefs = applicationContext.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    fun isFirstTime(): Resource<Boolean> {

        return try {
            val data = prefs.getBoolean(getString(R.string.preference_is_first_time), true)
            Resource.Success(data)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.SYSTEM_ERROR,
                message = e.message.toString()
            )
        }
    }

    fun setFirstTime(isFirst: Boolean): Resource<Unit> {

        return try {
            with(prefs.edit()) {
                putBoolean(getString(R.string.preference_is_first_time), isFirst)
                apply()
            }
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.SYSTEM_ERROR,
                message = e.message.toString()
            )
        }
    }

    fun getActiveScheduleId(): Int {
        return prefs.getInt(getString(R.string.active_schedule_id), -1)
    }

    fun setActiveScheduleId(id: Int): Resource<Unit> {
        return try {
            with(prefs.edit()) {
                putInt(getString(R.string.active_schedule_id), id)
                apply()
            }
            Resource.Success(null)
        } catch (e: Exception) {
            Resource.Error(
                errorType = Resource.SYSTEM_ERROR,
                message = e.message.toString()
            )
        }
    }

}


