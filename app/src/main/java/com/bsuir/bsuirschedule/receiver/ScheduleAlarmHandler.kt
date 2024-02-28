package com.bsuir.bsuirschedule.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.utils.WidgetSubjectController
import com.bsuir.bsuirschedule.service.ScheduleWidgetService

class ScheduleAlarmHandler(
    private val context: Context,
    private val schedule: Schedule
) {

    fun setAlarmManager() {
        val intent = Intent(context, ScheduleWidgetService::class.java)

        val sender: PendingIntent = PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val widgetSubjectController = WidgetSubjectController(schedule = schedule)

        val nextCallMillis = widgetSubjectController.getNextCallTime()

        if (nextCallMillis != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextCallMillis, sender)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextCallMillis, sender)
//            } else {
//                alarmManager.set(AlarmManager.RTC_WAKEUP, nextCallMillis, sender)
//            }
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