package com.bsuir.bsuirschedule.receiver

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.models.WidgetSettings
import com.bsuir.bsuirschedule.domain.usecase.schedule.WidgetManagerUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WidgetAddReceiver: BroadcastReceiver(), KoinComponent {

    private val widgetManagerUseCase: WidgetManagerUseCase by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        val widgetAddedText = context?.resources?.getString(R.string.widget_successfully_added) ?: "Widget successfully added"
        val isDarkTheme = intent.extras?.getBoolean(WidgetSettings.EXTRA_APPWIDGET_IS_DARK_THEME, true) ?: true
        val widgetId = intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID) ?: AppWidgetManager.INVALID_APPWIDGET_ID
        val scheduleId = intent.extras?.getInt(WidgetSettings.EXTRA_APPWIDGET_SCHEDULE_ID, -1) ?: -1
        val widgetSettings = WidgetSettings(
            widgetId,
            scheduleId,
            isDarkTheme
        )
        runBlocking(Dispatchers.IO) {
            widgetManagerUseCase.saveWidgetSettings(widgetSettings)
        }
        Toast.makeText(context, widgetAddedText, Toast.LENGTH_SHORT).show()
    }

}


