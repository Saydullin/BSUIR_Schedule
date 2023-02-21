package com.bsuir.bsuirschedule.presentation.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.content.ContextCompat
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.models.EmployeeSubject
import com.bsuir.bsuirschedule.domain.models.GroupSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.WidgetSettings
import com.bsuir.bsuirschedule.domain.usecase.schedule.GetActualScheduleDayUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.WidgetManagerUseCase
import com.bsuir.bsuirschedule.presentation.activities.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.collections.ArrayList

class MainWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return StackRemoteViewsFactory(this.applicationContext, intent)
    }
}

class StackRemoteViewsFactory(
    private val context: Context,
    val intent: Intent
) : RemoteViewsService.RemoteViewsFactory, KoinComponent {

    private val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
    private val scheduleId = intent.getIntExtra(WidgetSettings.EXTRA_APPWIDGET_SCHEDULE_ID, -1)
    private val mainActivityIntent = Intent(context, MainActivity::class.java)
    private val pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)
    private val getActualScheduleDayUseCase: GetActualScheduleDayUseCase by inject()
    private val widgetManagerUseCase: WidgetManagerUseCase by inject()
    private val scheduleSubjectsList = ArrayList<ScheduleSubject>()
    private var isGroupSchedule = false
    private var isDarkTheme = true

    override fun onCreate() {
        runBlocking(Dispatchers.IO) {
            val widgetSettings = widgetManagerUseCase.getWidgetSettings(widgetId)
            isDarkTheme = widgetSettings?.isDarkTheme ?: true
            val widgetSchedule = getActualScheduleDayUseCase.execute(scheduleId)
            if (widgetSchedule.schedule != null) {
                isGroupSchedule = widgetSchedule.schedule.isGroup()
            }
            if (widgetSchedule.activeScheduleDay != null) {
                scheduleSubjectsList.addAll(widgetSchedule.activeScheduleDay.schedule)
            }
        }
    }

    override fun onDataSetChanged() {
    }

    override fun onDestroy() {
        scheduleSubjectsList.clear()
    }

    override fun getCount(): Int {
        return scheduleSubjectsList.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val subject = scheduleSubjectsList[position]
        val remoteViews = RemoteViews(context.packageName, R.layout.main_widget_list_item)
        val subjectSubgroup = subject.getNumSubgroup()

        setWidgetThemeViews(context, remoteViews, isDarkTheme)

        remoteViews.setOnClickPendingIntent(R.id.list_item_root, pendingIntent)
        remoteViews.setTextViewText(R.id.startLessonTime, subject.startLessonTime)
        remoteViews.setTextViewText(R.id.endLessonTime, subject.endLessonTime)
        remoteViews.setTextViewText(R.id.lesson_title, subject.subject)
        if (subject.isActual == true) {
            remoteViews.setViewVisibility(R.id.subject_current_icon, View.VISIBLE)
        }
        remoteViews.setTextViewText(R.id.lesson_audience, subject.getAudienceInLine())
        if (subjectSubgroup == 0) {
            remoteViews.setViewVisibility(R.id.subgroup_container, View.GONE)
        } else {
            remoteViews.setTextViewText(R.id.lesson_subgroup, subject.getNumSubgroup().toString())
        }
        if (isGroupSchedule) {
            val employeeTitle = getSubjectEmployeeText(subject.employees ?: ArrayList())
            remoteViews.setTextViewText(R.id.lesson_teacher_name, employeeTitle)
        } else {
            val groupTitle = getSubjectGroupText(subject.subjectGroups ?: ArrayList())
            remoteViews.setTextViewText(R.id.lesson_teacher_name, groupTitle)
        }

        when (subject.lessonTypeAbbrev) {
            ScheduleSubject.LESSON_TYPE_LABORATORY -> {
                remoteViews.setImageViewResource(R.id.subject_type_icon, R.drawable.ic_subject_labaroty)
            }
            ScheduleSubject.LESSON_TYPE_PRACTISE -> {
                remoteViews.setImageViewResource(R.id.subject_type_icon, R.drawable.ic_subject_practise)
            }
            ScheduleSubject.LESSON_TYPE_LECTURE -> {
                remoteViews.setImageViewResource(R.id.subject_type_icon, R.drawable.ic_subject_lecture)
            }
            ScheduleSubject.LESSON_TYPE_CONSULTATION -> {
                remoteViews.setImageViewResource(R.id.subject_type_icon, R.drawable.ic_subject_lecture)
            }
            ScheduleSubject.LESSON_TYPE_PRETEST -> {
                remoteViews.setImageViewResource(R.id.subject_type_icon, R.drawable.ic_subject_lecture)
            }
            ScheduleSubject.LESSON_TYPE_EXAM -> {
                remoteViews.setImageViewResource(R.id.subject_type_icon, R.drawable.ic_subject_lecture)
            }
            else -> {
                remoteViews.setImageViewResource(R.id.subject_type_icon, R.drawable.ic_subject_lecture)
            }
        }

        return remoteViews
    }

    private fun setWidgetThemeViews(context: Context, remoteViews: RemoteViews, isDarkTheme: Boolean) {
        if (isDarkTheme) {
            remoteViews.setInt(R.id.list_item_root, "setBackgroundColor", ContextCompat.getColor(context, R.color.grey))
            remoteViews.setInt(R.id.subject_subgroup_icon, "setBackgroundResource", R.drawable.widget_subgroup)
            remoteViews.setInt(R.id.lesson_title, "setTextColor", ContextCompat.getColor(context, R.color.white_hint))
            remoteViews.setInt(R.id.lesson_audience, "setTextColor", ContextCompat.getColor(context, R.color.white_hint))
            remoteViews.setInt(R.id.lesson_subgroup, "setTextColor", ContextCompat.getColor(context, R.color.white_hint))
            remoteViews.setInt(R.id.lesson_teacher_name, "setTextColor", ContextCompat.getColor(context, R.color.text_hint))
            remoteViews.setInt(R.id.startLessonTime, "setTextColor", ContextCompat.getColor(context, R.color.white_hint))
            remoteViews.setInt(R.id.endLessonTime, "setTextColor", ContextCompat.getColor(context, R.color.white_hint))
        } else {
            remoteViews.setInt(R.id.list_item_root, "setBackgroundColor", ContextCompat.getColor(context, R.color.white_hint))
            remoteViews.setInt(R.id.subject_subgroup_icon, "setBackgroundResource", R.drawable.widget_dark_subgroup)
            remoteViews.setInt(R.id.lesson_title, "setTextColor", ContextCompat.getColor(context, R.color.grey))
            remoteViews.setInt(R.id.lesson_audience, "setTextColor", ContextCompat.getColor(context, R.color.grey))
            remoteViews.setInt(R.id.lesson_subgroup, "setTextColor", ContextCompat.getColor(context, R.color.grey))
            remoteViews.setInt(R.id.lesson_teacher_name, "setTextColor", ContextCompat.getColor(context, R.color.text_dark_hint))
            remoteViews.setInt(R.id.startLessonTime, "setTextColor", ContextCompat.getColor(context, R.color.grey))
            remoteViews.setInt(R.id.endLessonTime, "setTextColor", ContextCompat.getColor(context, R.color.grey))
        }
    }

    override fun getLoadingView(): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.main_widget_list_item)
        remoteViews.setTextViewText(R.id.lesson_title, context.getString(R.string.load))
        remoteViews.setTextViewText(R.id.startLessonTime, "00:00")
        remoteViews.setTextViewText(R.id.endLessonTime, "00:00")
        remoteViews.setTextViewText(R.id.lesson_audience, "000-0 к.")
        remoteViews.setTextViewText(R.id.lesson_teacher_name, context.getString(R.string.unknown))

        return remoteViews
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    private fun getSubjectEmployeeText(employees: ArrayList<EmployeeSubject>): String {
        return if (employees.isNotEmpty()) {
            employees[0].getName() + if (employees.size > 1) {
                val moreText = context.getString(R.string.more)
                ", $moreText ${employees.size - 1}"
            } else ""
        } else {
            context.getString(R.string.no_teacher_title)
        }
    }

    private fun getSubjectGroupText(subjectGroupList: ArrayList<GroupSubject>): String {
        return if (subjectGroupList.isNotEmpty()) {
            subjectGroupList[0].name + if (subjectGroupList.size > 1) {
                val moreText = context.getString(R.string.more)
                ", $moreText ${subjectGroupList.size - 1}"
            } else ""
        } else {
            context.getString(R.string.no_group_title)
        }
    }

}
