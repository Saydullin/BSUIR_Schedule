package com.bsuir.bsuirschedule.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.bsuir.bsuirschedule.service.ScheduleUpdateService
import java.text.SimpleDateFormat
import java.util.*

class ScheduleUpdateAlarmHandler(
    private val context: Context
) {

    fun setAlarmManager() {
        val intent = Intent(context, ScheduleUpdateService::class.java)

        val sender: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance(Locale("ru", "BY"))
        val calendarAlarm = Calendar.getInstance(Locale("ru", "BY"))
        val format = SimpleDateFormat("HH:mm")
        calendar.time = format.parse("20:00") as Date
        calendarAlarm.add(Calendar.DATE, 1)
        calendarAlarm.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY))
        calendarAlarm.set(Calendar.HOUR, calendar.get(Calendar.HOUR))
        calendarAlarm.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE))

        val timeInMillis = calendarAlarm.timeInMillis

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender)
//        } else {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender)
//        }
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