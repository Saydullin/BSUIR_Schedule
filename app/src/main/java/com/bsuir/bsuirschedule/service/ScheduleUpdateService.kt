package com.bsuir.bsuirschedule.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bsuir.bsuirschedule.presentation.widgets.WakeLocker
import com.bsuir.bsuirschedule.receiver.ScheduleUpdater

class ScheduleUpdateService : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        WakeLocker.acquire(context)

        val widgetIntent = Intent(context, ScheduleUpdater::class.java)
        widgetIntent.action = "com.bsuir.bsuirschedule.action.scheduleUpdater"
        context.sendBroadcast(widgetIntent)

        WakeLocker.release()
    }

}


