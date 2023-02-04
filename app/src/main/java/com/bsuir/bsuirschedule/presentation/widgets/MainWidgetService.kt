package com.bsuir.bsuirschedule.presentation.widgets

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.usecase.SharedPrefsUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.GetActualScheduleDayUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.GetScheduleUseCase
import com.bsuir.bsuirschedule.domain.utils.Resource
import com.bsuir.bsuirschedule.presentation.activities.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
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

    private val scheduleId = intent.getIntExtra("schedule_id", -1)
    private val mainActivityIntent = Intent(context, MainActivity::class.java)
    private val pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0)
    private val getActualScheduleDayUseCase: GetActualScheduleDayUseCase by inject()
    private val scheduleSubjectsList = ArrayList<ScheduleSubject>()

    override fun onCreate() {
        // Get schedule here
        runBlocking {
            launch(Dispatchers.IO) {
                val widgetSchedule = getActualScheduleDayUseCase.execute()
                if (widgetSchedule.activeScheduleDay != null) {
                    scheduleSubjectsList.addAll(widgetSchedule.activeScheduleDay.schedule)
                }
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
        remoteViews.setOnClickPendingIntent(R.id.list_item_root, pendingIntent)
        remoteViews.setTextViewText(R.id.startLessonTime, subject.startLessonTime)
        remoteViews.setTextViewText(R.id.endLessonTime, subject.endLessonTime)
        remoteViews.setTextViewText(R.id.lesson_title, subject.subject)
        if (subject.isActual == true) {
            remoteViews.setViewVisibility(R.id.subject_current_icon, View.VISIBLE)
        }
        remoteViews.setTextViewText(R.id.lesson_audience, subject.getAudienceInLine())

        when (subject.lessonTypeAbbrev) {
            ScheduleSubject.LABORATORY -> {
                remoteViews.setImageViewResource(R.id.subject_type_icon, R.drawable.ic_subject_labaroty)
            }
            ScheduleSubject.PRACTISE -> {
                remoteViews.setImageViewResource(R.id.subject_type_icon, R.drawable.ic_subject_practise)
            }
            ScheduleSubject.LECTURE -> {
                remoteViews.setImageViewResource(R.id.subject_type_icon, R.drawable.ic_subject_lecture)
            }
            ScheduleSubject.CONSULTATION -> {
                remoteViews.setImageViewResource(R.id.subject_type_icon, R.drawable.ic_subject_lecture)
            }
            ScheduleSubject.PRETEST -> {
                remoteViews.setImageViewResource(R.id.subject_type_icon, R.drawable.ic_subject_lecture)
            }
            ScheduleSubject.EXAM -> {
                remoteViews.setImageViewResource(R.id.subject_type_icon, R.drawable.ic_subject_lecture)
            }
            else -> {
                remoteViews.setImageViewResource(R.id.subject_type_icon, R.drawable.ic_subject_lecture)
            }
        }

        return remoteViews
    }

    override fun getLoadingView(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.main_widget_list_item)
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

}
