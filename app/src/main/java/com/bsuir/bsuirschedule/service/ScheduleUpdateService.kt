package com.bsuir.bsuirschedule.service

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.utils.ScheduleUpdateManager
import com.bsuir.bsuirschedule.presentation.activities.MainActivity
import com.bsuir.bsuirschedule.presentation.widgets.ScheduleWidget
import com.bsuir.bsuirschedule.presentation.widgets.WakeLocker
import com.bsuir.bsuirschedule.receiver.ScheduleUpdater
import io.karn.notify.Notify
import org.koin.core.component.inject

class ScheduleUpdateService : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Log.e("sady", "ScheduleUpdateService received")

        WakeLocker.acquire(context)

        val widgetIntent = Intent(context, ScheduleUpdater::class.java)
        widgetIntent.action = "com.bsuir.bsuirschedule.action.scheduleUpdater"
        context.sendBroadcast(widgetIntent)

        WakeLocker.release()
    }

}


