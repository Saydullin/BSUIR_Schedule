package com.bsuir.bsuirschedule.domain.repository

import android.content.Context

interface SharedPrefsRepository {

    val context: Context

    fun isFirstTime(): Boolean

    fun setFirstTime(isFirst: Boolean)

    fun isDataLoadingTrouble(): Boolean

    fun setDataLoadingTrouble(isTrouble: Boolean)

    fun getActiveScheduleId(): Int

    fun setActiveScheduleId(scheduleId: Int)

}