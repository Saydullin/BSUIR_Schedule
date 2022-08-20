package com.example.bsuirschedule.domain.repository

import android.content.Context

interface SharedPrefsRepository {

    val context: Context

    fun isFirstTime(): Boolean

    fun setFirstTime(isFirst: Boolean)

    fun getActiveScheduleId(): Int

    fun setActiveScheduleId(scheduleId: Int)

}