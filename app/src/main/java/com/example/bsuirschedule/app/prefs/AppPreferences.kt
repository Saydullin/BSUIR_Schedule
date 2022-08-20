package com.example.bsuirschedule.app.prefs

import com.example.bsuirschedule.domain.utils.Resource

interface AppPreferences {

    fun isFirstTime(): Resource<Boolean>

    fun setFirstTime(isFirst: Boolean): Resource<Unit>

    fun getActiveScheduleId(): Int

    fun setActiveScheduleId(id: Int): Resource<Unit>

}