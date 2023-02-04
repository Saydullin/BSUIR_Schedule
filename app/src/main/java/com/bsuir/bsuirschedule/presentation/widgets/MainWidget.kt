package com.bsuir.bsuirschedule.presentation.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.bsuir.bsuirschedule.R

/**
 * Implementation of App Widget functionality.
 */
class MainWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val views = RemoteViews(context.packageName, R.layout.main_widget)
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, MainWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            views.setRemoteAdapter(appWidgetId, R.id.itemsListView, intent)
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

//internal fun updateAppWidget(
//    context: Context,
//    appWidgetManager: AppWidgetManager,
//    appWidgetId: Int
//) {
//    // Construct the RemoteViews object
////    views.setTextViewText(R.id.appwidget_text, widgetText)
//    // There may be multiple widgets active, so update all of them
//    val views = RemoteViews(context.packageName, R.layout.main_widget)
//    val intent = Intent(context, MainWidgetService::class.java)
//    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
//    intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
//    views.setRemoteAdapter(appWidgetId, R.id.itemsListView, intent)
//    // Instruct the widget manager to update the widget
//    appWidgetManager.updateAppWidget(appWidgetId, views)
//}


