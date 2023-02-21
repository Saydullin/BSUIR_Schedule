package com.bsuir.bsuirschedule.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.usecase.SharedPrefsUseCase
import com.bsuir.bsuirschedule.domain.utils.ScheduleUpdateManager
import com.bsuir.bsuirschedule.presentation.activities.MainActivity
import io.karn.notify.Notify
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ScheduleUpdater : BroadcastReceiver(), KoinComponent {

    private val sharedPrefsUseCase: SharedPrefsUseCase by inject()
    private val scheduleUpdateManager: ScheduleUpdateManager by inject()

    private fun buildScheduleUpdateNotification(context: Context, titleText: String, messageText: String) {
        Notify
            .with(context)
            .meta {
                clickIntent = PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
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
                    PendingIntent.FLAG_IMMUTABLE
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

    private fun notifyAboutUpdates(updatedSchedules: ArrayList<SavedSchedule>, context: Context?) {

        updatedSchedules.forEach { updatedSchedule ->
            if (updatedSchedule.isUpdatedSuccessfully) {
                if (context == null) return@forEach
                if (updatedSchedule.isGroup) {
                    buildScheduleUpdateNotification(
                        context,
                        context.getString(R.string.schedule_updated),
                        context.getString(R.string.group_schedule_updated_notification, updatedSchedule.group.name)
                    )
                } else {
                    buildScheduleUpdateNotification(
                        context,
                        context.getString(R.string.schedule_updated),
                        context.getString(R.string.employee_schedule_updated_notification, updatedSchedule.employee.getName())
                    )
                }
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val isNotificationsEnable = sharedPrefsUseCase.isNotificationsEnabled()

        val updatedSchedules = scheduleUpdateManager.updatedSchedules()
        if (isNotificationsEnable) {
            notifyAboutUpdates(updatedSchedules, context)
        }

        if (context != null) {
            val scheduleUpdateAlarmHandler = ScheduleUpdateAlarmHandler(context)
            scheduleUpdateAlarmHandler.cancelAlarmManager()
            scheduleUpdateAlarmHandler.setAlarmManager()
        }
    }
}


