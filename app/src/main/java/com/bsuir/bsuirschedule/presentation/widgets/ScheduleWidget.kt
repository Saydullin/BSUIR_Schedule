package com.bsuir.bsuirschedule.presentation.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.usecase.schedule.GetActualScheduleDayUseCase
import com.bsuir.bsuirschedule.domain.utils.CalendarDate
import com.bsuir.bsuirschedule.presentation.activities.MainActivity
import com.bsuir.bsuirschedule.receiver.AlarmHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ScheduleWidget : AppWidgetProvider(), KoinComponent {

    private val getActualScheduleDayUseCase: GetActualScheduleDayUseCase by inject()

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int) {

        val mainActivityIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0)

        val remoteViews = RemoteViews(context.packageName, R.layout.today_schedule_widget)
        val calendar = Calendar.getInstance()
        val widgetText = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"

        remoteViews.setOnClickPendingIntent(R.id.schedule_widget_root, pendingIntent)
        remoteViews.setTextViewText(R.id.appwidget_text, widgetText)

        runBlocking(Dispatchers.IO) {
            val widgetSchedule = getActualScheduleDayUseCase.execute()
            val currentSchedule = widgetSchedule.schedule ?: return@runBlocking
            val actualScheduleDay = widgetSchedule.activeScheduleDay

            if (currentSchedule.selectedSubgroup == 0) {
                val allSubgroupsText = context.getString(R.string.all_subgroups_short)
                remoteViews.setTextViewText(R.id.subgroup_number, allSubgroupsText)
            } else {
                remoteViews.setTextViewText(R.id.subgroup_number, currentSchedule.selectedSubgroup.toString())
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
                    R.plurals.plural_lessons,
                    actualScheduleDay.schedule.size,
                    actualScheduleDay.schedule.size
                )
                remoteViews.setTextViewText(R.id.schedule_lessons_amount, scheduleLessonsText)
            }
            val intent = Intent(context, MainWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.putExtra("unique", Date().time)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
            remoteViews.setRemoteAdapter(R.id.schedule_listView, intent)
        }

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

        val alarmHandler = AlarmHandler(context)
        alarmHandler.cancelAlarmManager()
        alarmHandler.setAlarmManager()

        Log.e("sady", "WIDGET updated!")
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
        val alarmHandler = AlarmHandler(context)
        alarmHandler.cancelAlarmManager()

        Log.e("sady", "WIDGET disabled!")
    }

}