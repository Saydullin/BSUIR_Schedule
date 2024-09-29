package com.bsuir.bsuirschedule.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class ScheduleNotificationChannel(
    private val context: Context
) {

    fun create(
        channelId: String,
        channelName: String,
        channelDescription: String,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT
    ) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            importance
        ).apply {
            description = channelDescription
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}


