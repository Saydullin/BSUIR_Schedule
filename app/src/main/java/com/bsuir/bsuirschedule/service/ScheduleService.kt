package com.bsuir.bsuirschedule.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import com.bsuir.bsuirschedule.receiver.AlarmReceiver

class ScheduleService(
    private val context: Context
) {
    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setRepeatAlarm() {
        setAlarm(alarmIntent = getAlarmIntent())
    }

    private fun setAlarm(alarmIntent: PendingIntent) {
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime(),
            AlarmManager.INTERVAL_HALF_HOUR,
            alarmIntent
        )
    }

    private fun getAlarmIntent(): PendingIntent {
        return Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
        }
    }

}


