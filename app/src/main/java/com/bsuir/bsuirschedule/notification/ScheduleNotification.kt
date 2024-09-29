package com.bsuir.bsuirschedule.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.presentation.activities.MainActivity

class ScheduleNotification(
    private val context: Context,
    private val notification: Notification?,
    private val priority: Int = NotificationCompat.PRIORITY_LOW,
) {

    private lateinit var summaryNotificationBuilder: NotificationCompat.Builder

    init {
        initSummaryNotification()
    }

    companion object {
        const val SUMMARY_ID = 0
        const val NOTIFICATION_PERMISSION_CODE = 2
        const val SCHEDULE_UPDATE_NOTIFICATION_GROUP = "com.bsuir.bsuirschedule.SCHEDULE_UPDATE"

        inline fun build(
            context: Context,
            builder: Builder.() -> Unit
        ) = Builder(context).apply(builder).build()
    }

    private fun initSummaryNotification() {
        summaryNotificationBuilder = NotificationCompat.Builder(
            context,
            context.getString(R.string.notification_update_channel_id)
        )
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Hello group")
            .setContentText("Group notifications")
            .setPriority(priority)
            .setGroup(SCHEDULE_UPDATE_NOTIFICATION_GROUP)
            .setGroupSummary(true)
            .setAutoCancel(true)
    }

    class Builder(
        private val context: Context
    ) {
        private var contentTitle = ""
        private var contentText = ""
        private var priority: Int = NotificationCompat.PRIORITY_DEFAULT
        private var autoCancel = true
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        private val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        fun setTitle(title: String): Builder {
            this.contentTitle = title
            return this
        }

        fun setText(text: String): Builder {
            this.contentText = text
            return this
        }

        fun setPriority(priority: Int): Builder {
            this.priority = priority
            return this
        }

        fun setAutoCancel(autoCancel: Boolean): Builder {
            this.autoCancel = autoCancel
            return this
        }

        fun build(): ScheduleNotification {

            val builder = NotificationCompat.Builder(
                context,
                context.getString(R.string.notification_update_channel_id)
            )
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setPriority(priority)
                .setGroup(SCHEDULE_UPDATE_NOTIFICATION_GROUP)
                .setContentIntent(pendingIntent)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)

            return ScheduleNotification(
                context = context,
                notification = builder.build()
            )
        }

    }

    fun show(notificationId: Int) {
        if (notification == null) return

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
            notify(SUMMARY_ID, summaryNotificationBuilder.build())
        }
    }

}


