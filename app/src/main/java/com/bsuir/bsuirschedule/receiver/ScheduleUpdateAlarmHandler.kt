package com.bsuir.bsuirschedule.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.bsuir.bsuirschedule.service.ScheduleUpdateService
import java.util.*

class ScheduleUpdateAlarmHandler(
    private val context: Context
) {

    fun setAlarmManager() {
        val intent = Intent(context, ScheduleUpdateService::class.java)

        val sender: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 18)
        val timeInMillis = calendar.timeInMillis

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, sender)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender)
        }
    }

    fun cancelAlarmManager() {
        val intent = Intent(context, ScheduleUpdateService::class.java)

        val sender: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(context, 2, intent, 0)
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(sender)
    }

}