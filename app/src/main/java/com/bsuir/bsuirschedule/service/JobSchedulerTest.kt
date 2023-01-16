package com.bsuir.bsuirschedule.service

import android.app.Notification
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.presentation.activities.MainActivity
import io.karn.notify.Notify


class JobSchedulerTest : JobService() {

    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.e("sady", "Job Started")

        doBackgroundWork()
        doForegroundWork()
        return false
    }

    private fun doBackgroundWork() {
        Log.e("sady", "Job Executed")
//        buildNotification(applicationContext, "Test Title", "test description")
    }

    private fun doForegroundWork() {
        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
        )

        val notification: Notification = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Hi Saydullin!")
            .setContentText("Doing some work...")
            .setContentIntent(pendingIntent).build()

        startForeground(1337, notification)

        buildNotification(applicationContext, "Test Title", "test description")
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

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.e("sady", "Job is stopped")
        Log.e("sady", "$p0")

        jobFinished(p0, true);
        return false
    }

}


