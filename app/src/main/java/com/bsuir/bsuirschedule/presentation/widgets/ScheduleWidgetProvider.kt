package com.bsuir.bsuirschedule.presentation.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import androidx.core.util.SizeFCompat

class ScheduleWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        if (appWidgetManager == null) return
        if (appWidgetIds == null) return

        for (appWidgetId in appWidgetIds) {
            val supportedSizes = listOf(
                SizeFCompat(180.0f, 110.0f),
                SizeFCompat(270.0f, 110.0f),
                SizeFCompat(270.0f, 280.0f),
            )
        }
    }

}