package com.bsuir.bsuirschedule.service

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.bsuir.bsuirschedule.presentation.widgets.ScheduleWidget
import com.bsuir.bsuirschedule.presentation.widgets.WakeLocker

class ScheduleWidgetService : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        WakeLocker.acquire(context)

        val widgetIntent = Intent(context, ScheduleWidget::class.java)
        widgetIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, ScheduleWidget::class.java))
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(widgetIntent)

        WakeLocker.release()

    }

}


