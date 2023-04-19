package com.bsuir.bsuirschedule.presentation.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.models.Schedule
import com.bsuir.bsuirschedule.domain.models.WidgetSettings
import com.bsuir.bsuirschedule.domain.usecase.schedule.GetActualScheduleDayUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.WidgetManagerUseCase
import com.bsuir.bsuirschedule.domain.utils.CalendarDate
import com.bsuir.bsuirschedule.presentation.activities.MainActivity
import com.bsuir.bsuirschedule.receiver.ScheduleAlarmHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ScheduleWidget : AppWidgetProvider(), KoinComponent {

    private val widgetManagerUseCase: WidgetManagerUseCase by inject()
    private val getActualScheduleDayUseCase: GetActualScheduleDayUseCase by inject()
    private var currentSchedule: Schedule = Schedule.empty

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int) {

        val mainActivityIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)

        val remoteViews = RemoteViews(context.packageName, R.layout.today_schedule_widget)

        remoteViews.setOnClickPendingIntent(R.id.schedule_widget_root, pendingIntent)

        runBlocking(Dispatchers.IO) {
            /** widget configuration settings */
            val widgetSettings = widgetManagerUseCase.getWidgetSettings(appWidgetId)
            setWidgetThemeViews(context, remoteViews, true)
            val scheduleId = widgetSettings?.scheduleId ?: -1
            val isWidgetDarkTheme = widgetSettings?.isDarkTheme ?: true
            setWidgetThemeViews(context, remoteViews, isWidgetDarkTheme)

            /** widget configuration settings */
            val widgetSchedule = getActualScheduleDayUseCase.execute(scheduleId)
            currentSchedule = widgetSchedule.schedule ?: return@runBlocking
            val actualScheduleDay = widgetSchedule.activeScheduleDay
            val scheduleSubgroup = currentSchedule.settings.subgroup.selectedNum

            if (scheduleSubgroup == 0) {
                val allSubgroupsText = context.getString(R.string.all_subgroups_short)
                remoteViews.setTextViewText(R.id.subgroup_number, allSubgroupsText)
            } else {
                remoteViews.setTextViewText(R.id.subgroup_number, scheduleSubgroup.toString())
            }
            if (!widgetSchedule.isScheduleEmpty) {
                val noScheduleText = context.getString(R.string.no_schedule, currentSchedule.getTitle())
                remoteViews.setTextViewText(R.id.appwidget_text, noScheduleText)
                remoteViews.setViewVisibility(R.id.schedule_listView, View.GONE)
                remoteViews.setViewVisibility(R.id.schedule_day_header, View.GONE)
                return@runBlocking
            }
            remoteViews.setTextViewText(R.id.appwidget_text, currentSchedule.getTitle())
            if (actualScheduleDay != null) {
                when (actualScheduleDay.date) {
                    CalendarDate.YESTERDAY -> {
                        remoteViews.setTextViewText(R.id.schedule_date, context.getString(R.string.yesterday))
                    }
                    CalendarDate.TODAY -> {
                        remoteViews.setTextViewText(R.id.schedule_date, context.getString(R.string.today))
                    }
                    CalendarDate.TOMORROW -> {
                        remoteViews.setTextViewText(R.id.schedule_date, context.getString(R.string.tomorrow))
                    }
                    else -> {
                        remoteViews.setTextViewText(R.id.schedule_date, actualScheduleDay.date)
                    }
                }
                remoteViews.setTextViewText(R.id.schedule_day_of_week, actualScheduleDay.weekDayNameUpperFirstLetter())
                val scheduleLessonsText = context.resources.getQuantityString(
                    R.plurals.plural_lessons_short,
                    actualScheduleDay.schedule.size,
                    actualScheduleDay.schedule.size
                )
                remoteViews.setTextViewText(R.id.schedule_lessons_amount, scheduleLessonsText)
            }
            val intent = Intent(context, MainWidgetService::class.java)
            intent.putExtra(WidgetSettings.EXTRA_APPWIDGET_SCHEDULE_ID, scheduleId)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.putExtra("unique", Date().time)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            remoteViews.setRemoteAdapter(R.id.schedule_listView, intent)
        }

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

        val scheduleAlarmHandler = ScheduleAlarmHandler(context, currentSchedule)
        scheduleAlarmHandler.cancelAlarmManager()
        scheduleAlarmHandler.setAlarmManager()
    }

    private fun setWidgetThemeViews(context: Context, remoteViews: RemoteViews, isDarkTheme: Boolean) {
        if (isDarkTheme) {
            remoteViews.setInt(R.id.schedule_widget_body, "setBackgroundResource", R.drawable.widget_dark_holder)
            remoteViews.setInt(R.id.subgroup_number_icon, "setBackgroundResource", R.drawable.widget_subgroup)
            remoteViews.setInt(R.id.appwidget_text, "setTextColor", ContextCompat.getColor(context, R.color.white_hint))
            remoteViews.setInt(R.id.subgroup_number, "setTextColor", ContextCompat.getColor(context, R.color.white_hint))
        } else {
            remoteViews.setInt(R.id.schedule_widget_body, "setBackgroundResource", R.drawable.widget_holder)
            remoteViews.setInt(R.id.subgroup_number_icon, "setBackgroundResource", R.drawable.widget_dark_subgroup)
            remoteViews.setInt(R.id.appwidget_text, "setTextColor", ContextCompat.getColor(context, R.color.dark))
            remoteViews.setInt(R.id.subgroup_number, "setTextColor", ContextCompat.getColor(context, R.color.dark))
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        runBlocking(Dispatchers.IO) {
            appWidgetIds?.forEach { appWidgetId ->
                widgetManagerUseCase.deleteWidgetSettings(appWidgetId)
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDisabled(context: Context) {
        val scheduleAlarmHandler = ScheduleAlarmHandler(context, currentSchedule)
        scheduleAlarmHandler.cancelAlarmManager()
    }

}


