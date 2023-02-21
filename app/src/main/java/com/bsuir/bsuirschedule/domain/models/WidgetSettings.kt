package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.WidgetSettingsTable

data class WidgetSettings (
    val id: Int,
    val scheduleId: Int,
    val isDarkTheme: Boolean
) {

    companion object {
        const val EXTRA_APPWIDGET_SCHEDULE_TITLE = "bsuir.appwidget.extra.APPWIDGET_SCHEDULE_TITLE"
        const val EXTRA_HAVE_TO_ADD_APPWIDGET = "bsuir.appwidget.extra.APPWIDGET_ADD"
        const val EXTRA_APPWIDGET_SCHEDULE_ID = "bsuir.appwidget.extra.APPWIDGET_SCHEDULE_ID"
        const val EXTRA_APPWIDGET_IS_DARK_THEME = "bsuir.appwidget.extra.APPWIDGET_IS_DARK_THEME"

        val empty = WidgetSettings(
            -1,
            -1,
            false
        )
    }

    fun toWidgetSettingsTable() = WidgetSettingsTable(
        id = id,
        scheduleId = scheduleId,
        isDarkTheme = isDarkTheme
    )

}


