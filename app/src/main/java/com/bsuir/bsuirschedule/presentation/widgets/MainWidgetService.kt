package com.bsuir.bsuirschedule.presentation.widgets

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.domain.models.ScheduleDay

class MainWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return StackRemoteViewsFactory(this.applicationContext, intent!!)
    }

}

class StackRemoteViewsFactory(
    private val context: Context,
    private val intent: Intent
) : RemoteViewsService.RemoteViewsFactory {

    private val scheduleDayList = ArrayList<ScheduleDay>()

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
    }

    override fun onDestroy() {
        scheduleDayList.clear()
    }

    override fun getCount(): Int {
//        return scheduleDayList.size
        return 10
    }

    override fun getViewAt(position: Int): RemoteViews {
        val removeViews = RemoteViews(context.packageName, R.layout.main_widget_list_item)
        removeViews.setTextViewText(R.id.text, "Hello")

        return removeViews
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

}
