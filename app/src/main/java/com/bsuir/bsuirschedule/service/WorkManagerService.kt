package com.bsuir.bsuirschedule.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.presentation.activities.MainActivity
import io.karn.notify.Notify

class WorkManagerService(
    private val context: Context,
    private val workParams: WorkerParameters,
) : Worker(context, workParams) {

    override fun doWork(): Result {
        Log.e("sady", "Hello from work manager!")

//        try {
//            buildNotification(applicationContext, "Hello from work manager!", "test description")
////            val workManager = WorkManager.getInstance(applicationContext)
////            val workInfo = workManager.getWorkInfosByTag("Sady")
////            Log.e("sady", "workInfo $workInfo")
//        } catch (e: Exception) {
//            Log.e("sady", "notify error ${e.message}")
//            return Result.failure()
//        }

        return Result.success()
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


