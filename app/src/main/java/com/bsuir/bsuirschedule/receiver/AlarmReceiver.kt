package com.bsuir.bsuirschedule.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.AlarmClock
import android.util.Log
import android.widget.Toast
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.utils.AlarmClockManager
import com.bsuir.bsuirschedule.presentation.activities.MainActivity
import io.karn.notify.Notify
import java.util.*

class AlarmReceiver: BroadcastReceiver() {

    private val alarmClockManager = AlarmClockManager()

    override fun onReceive(context: Context, intent: Intent?) {
        try {
            val alarmInitIntent = alarmClockManager.getAlarmIntent()
            context.startActivity(alarmInitIntent)
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            buildNotification(context, "Error", "${e.message}")
        }

        val alarmClockHandler = AlarmClockHandler(context)
        alarmClockHandler.cancelAlarmManager()
        alarmClockHandler.setAlarmManager()
    }

    private fun buildNotification(context: Context, titleText: String, messageText: String) {
        Notify
            .with(context)
            .meta {
                clickIntent = PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    0
                )
            }
            .content {
                title = titleText
                text = messageText
            }
            .stackable {
                clickIntent = PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    0
                )
                key = "schedule_notification_update_key"
                summaryContent = messageText
                summaryTitle = { count ->
                    val pluralSchedulesText = context.resources.getQuantityString(R.plurals.plural_schedules, count, count)
                    context.getString(R.string.schedule_updated_notification, pluralSchedulesText)
                }
                summaryDescription = { count -> context.getString(R.string.new_notifications_amount, count) }
            }
            .show()
    }

}







