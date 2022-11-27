package com.bsuir.bsuirschedule.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.AlarmClock
import android.util.Log
import android.widget.Toast
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.presentation.activities.MainActivity
import io.karn.notify.Notify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        try {
            val alarmInitIntent = Intent(AlarmClock.ACTION_SET_ALARM)
            alarmInitIntent.putExtra(AlarmClock.EXTRA_HOUR, 10)
            alarmInitIntent.putExtra(AlarmClock.EXTRA_MINUTES, 20)
            alarmInitIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true)
            alarmInitIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(alarmInitIntent)
            Log.e("sady", "Thread is finished")

            Log.e("sady", "RECEIVE SUCCESS")
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("sady", "RECEIVE ERROR")
            e.printStackTrace()
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
        buildNotification(context, "Test Title", "test description")
        Log.e("sady", "RECEIVED")
//        val scheduleUpdater = ScheduleUpdater()
//        val updatedSchedules = scheduleUpdater.execute()
//        if (updatedSchedules.isEmpty()) return
//        if (updatedSchedules.size == 1) {
//            val updatedSchedule = updatedSchedules[0]
//            if (updatedSchedule.isGroup) {
//                val groupScheduleUpdatedText = context.getString(R.string.group_schedule_updated)
//                buildNotification(context, groupScheduleUpdatedText, updatedSchedule.group.name)
//            } else {
//                val employeeScheduleUpdatedText = context.getString(R.string.employee_schedule_updated)
//                buildNotification(context, employeeScheduleUpdatedText, updatedSchedule.employee.getName())
//            }
//        } else {
//            val scheduleUpdatedText = context.getString(R.string.schedule_updated)
//            updatedSchedules.map { savedSchedule ->
//                if (savedSchedule.isGroup) {
//                    buildNotification(context, scheduleUpdatedText, savedSchedule.group.name)
//                } else {
//                    buildNotification(context, scheduleUpdatedText, savedSchedule.employee.getName())
//                }
//            }
//        }
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











