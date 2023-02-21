package com.bsuir.bsuirschedule.domain.repository

import com.bsuir.bsuirschedule.data.db.dao.WidgetSettingsDao
import com.bsuir.bsuirschedule.domain.models.WidgetSettings

interface WidgetSettingsRepository {

    val widgetSettingsDao: WidgetSettingsDao

    fun getWidgetSettings(widgetId: Int): WidgetSettings?

    fun saveWidgetSettings(widgetSettings: WidgetSettings)

    fun deleteWidgetSettings(widgetId: Int)

}