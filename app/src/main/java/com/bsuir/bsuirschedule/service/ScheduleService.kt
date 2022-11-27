package com.bsuir.bsuirschedule.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.provider.AlarmClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bsuir.bsuirschedule.receiver.AlarmReceiver

class ScheduleService(
    private val context: Context
) {
    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setRepeatAlarm() {
        setAlarm(alarmIntent = getAlarmIntent())
    }

    private fun setAlarm(alarmIntent: PendingIntent) {
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            SystemClock.elapsedRealtime(),
//            AlarmManager.INTERVAL_HALF_HOUR,
//            alarmIntent
//        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            SystemClock.elapsedRealtime() + 1000 * 3,
            alarmIntent
        )
    }

    private fun getAlarmIntent(): PendingIntent {
        return Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
        }
    }

}


