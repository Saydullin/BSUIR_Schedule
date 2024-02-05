package com.bsuir.bsuirschedule.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.models.SavedSchedule
import com.bsuir.bsuirschedule.domain.usecase.SharedPrefsUseCase
import com.bsuir.bsuirschedule.domain.utils.ScheduleUpdateManager
import com.bsuir.bsuirschedule.presentation.activities.MainActivity
import io.karn.notify.Notify
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ScheduleUpdateWorker(
    private val context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams), KoinComponent {

    private val sharedPrefsUseCase: SharedPrefsUseCase by inject()
    private val scheduleUpdateManager: ScheduleUpdateManager by inject()

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun doWork(): Result {

        val isNotificationsEnable = sharedPrefsUseCase.isNotificationsEnabled()

        GlobalScope.launch(Dispatchers.IO) {
            if (isNotificationsEnable) {
                try {
                    val updatedSchedules = scheduleUpdateManager.execute()
                    notifyAboutUpdates(updatedSchedules, context)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        return Result.success()
    }

    private fun notifyAboutUpdates(updatedSchedules: ArrayList<SavedSchedule>, context: Context) {

        updatedSchedules.map { updatedSchedule ->
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

    private fun buildScheduleUpdateNotification(context: Context, titleText: String, messageText: String) {
        Notify
            .with(context)
            .meta {
                clickIntent = PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_MUTABLE
                )
            }
            .alerting("schedule_update_channel_id") {
                lightColor = Color.RED
                channelName = "Schedule update"
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
                    PendingIntent.FLAG_MUTABLE
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


