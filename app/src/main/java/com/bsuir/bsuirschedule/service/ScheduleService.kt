package com.bsuir.bsuirschedule.service

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.bsuir.bsuirschedule.receiver.AlarmReceiver

class ScheduleService(
    private val context: Context
) {
    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setExactAlarm(timeInMillis: Long) {
        setAlarm(
            timeInMillis = timeInMillis,
            getPendingIntent(
                getIntent().apply {
//                    action = "ACTION_SET_EXACT_ALARM",
//                    putExtra("ACTION_SET_EXACT_ALARM", timeInMillis),
                    action = "ACTION_SET_REPETITIVE_EXACT"
                    putExtra("EXTRA_EXACT_ALARM_TIME", timeInMillis)
                }
            )
        )
    }

    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent) {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }

    private fun getIntent(): Intent = Intent(context, AlarmReceiver::class.java)

    private fun getPendingIntent(intent: Intent) =
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT,
        )

}


