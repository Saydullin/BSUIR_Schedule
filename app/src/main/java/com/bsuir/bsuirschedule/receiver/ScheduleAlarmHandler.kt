package com.bsuir.bsuirschedule.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.utils.WidgetSubjectController
import com.bsuir.bsuirschedule.service.ScheduleWidgetService
import java.util.*
import kotlin.math.roundToInt

class ScheduleAlarmHandler(
    private val context: Context,
    private val schedule: Schedule
) {

    fun setAlarmManager() {
        val intent = Intent(context, ScheduleWidgetService::class.java)

        val sender: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(context, 2, intent, 0)
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val widgetSubjectController = WidgetSubjectController(schedule = schedule)

        val nextCallMillis = widgetSubjectController.getNextCallTime()

//        val calendar = Calendar.getInstance()
//        var minutes = calendar.get(Calendar.MINUTE)
//        val decTen = minutes % 5
//        if (decTen < 3) {
//            minutes += 3
//        }
//        val extraMinutes = (minutes / 5.0).roundToInt() * 5
//        calendar.add(Calendar.MINUTE, extraMinutes - calendar.get(Calendar.MINUTE))
//        calendar.set(Calendar.SECOND, 0)
//        val timeInMillis = calendar.timeInMillis

        if (nextCallMillis != null) {
            Log.e("sady", "nextCallMillis is $nextCallMillis")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextCallMillis, sender)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, nextCallMillis, sender)
            }
        } else {
            Log.e("sady", "nextCallMillis is null")
        }
    }

    fun cancelAlarmManager() {
        val intent = Intent(context, ScheduleWidgetService::class.java)

        val sender: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(context, 2, intent, 0)
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(sender)
    }

}