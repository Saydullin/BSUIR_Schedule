package com.bsuir.bsuirschedule.presentation.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import android.widget.Toast
import com.bsuir.bsuirschedule.R
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class TodayScheduleWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val calendar = Calendar.getInstance()
    val widgetText = "${calendar.get(Calendar.HOUR)}:${calendar.get(Calendar.MINUTE)} :${calendar.get(Calendar.SECOND)}"
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.today_schedule_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}