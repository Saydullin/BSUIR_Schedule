package com.bsuir.bsuirschedule.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.presentation.activities.MainActivity
import io.karn.notify.Notify

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val scheduleUpdater = ScheduleUpdater()
        val updatedSchedules = scheduleUpdater.execute()
        Log.e("sady", "updatedSchedules $updatedSchedules")
        if (updatedSchedules.isEmpty()) return
        if (updatedSchedules.size == 1) {
            val updatedSchedule = updatedSchedules[0]
            if (updatedSchedule.isGroup) {
                val groupScheduleUpdatedText = context.getString(R.string.group_schedule_updated)
                buildNotification(context, groupScheduleUpdatedText, updatedSchedule.group.name)
            } else {
                val employeeScheduleUpdatedText = context.getString(R.string.employee_schedule_updated)
                buildNotification(context, employeeScheduleUpdatedText, updatedSchedule.employee.getName())
            }
        } else {
            val scheduleUpdatedText = context.getString(R.string.schedule_updated)
            updatedSchedules.map { savedSchedule ->
                if (savedSchedule.isGroup) {
                    buildNotification(context, scheduleUpdatedText, savedSchedule.group.name)
                } else {
                    buildNotification(context, scheduleUpdatedText, savedSchedule.employee.getName())
                }
            }
        }
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











