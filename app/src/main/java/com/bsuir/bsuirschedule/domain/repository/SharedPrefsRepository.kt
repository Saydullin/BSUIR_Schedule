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

}