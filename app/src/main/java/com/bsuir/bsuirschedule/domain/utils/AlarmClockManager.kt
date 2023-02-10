package com.bsuir.bsuirschedule.domain.utils

import android.content.Intent
import android.provider.AlarmClock
import java.util.Calendar

class AlarmClockManager {

    fun getAlarmIntent(): Intent {
        val alarmInitIntent = Intent(AlarmClock.ACTION_SET_ALARM)
        alarmInitIntent.putExtra(AlarmClock.EXTRA_DAYS, arrayListOf(Calendar.SUNDAY, Calendar.MONDAY))
        alarmInitIntent.putExtra(AlarmClock.EXTRA_HOUR, 10)
        alarmInitIntent.putExtra(AlarmClock.EXTRA_MINUTES, 20)
        alarmInitIntent.putExtra(AlarmClock.EXTRA_MESSAGE, "КСиС через 2 часа")
        alarmInitIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        alarmInitIntent.putExtra(AlarmClock.EXTRA_IS_PM, true)
        alarmInitIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        return alarmInitIntent
    }

    fun searchAlarmClock(): Intent {
        val alarmInitIntent = Intent(AlarmClock.ALARM_SEARCH_MODE_TIME)
        alarmInitIntent.putExtra(AlarmClock.EXTRA_HOUR, 10)
        alarmInitIntent.putExtra(AlarmClock.EXTRA_MINUTES, 20)

        return alarmInitIntent
    }

}