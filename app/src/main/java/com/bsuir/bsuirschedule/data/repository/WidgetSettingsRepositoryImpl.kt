package com.bsuir.bsuirschedule.data.repository

import com.bsuir.bsuirschedule.data.db.dao.WidgetSettingsDao
import com.bsuir.bsuirschedule.domain.models.WidgetSettings
import com.bsuir.bsuirschedule.domain.repository.WidgetSettingsRepository

class WidgetSettingsRepositoryImpl(
    override val widgetSettingsDao: WidgetSettingsDao
) : WidgetSettingsRepository {

    override fun getWidgetSettings(widgetId: Int): WidgetSettings? {
        val widgetSettingTable = widgetSettingsDao.getWidgetSettings(widgetId)

        return widgetSettingTable?.toWidgetSettings()
    }

    override fun saveWidgetSettings(widgetSettings: WidgetSettings) {
        widgetSettingsDao.saveWidgetSettings(widgetSettings.toWidgetSettingsTable())
    }

    override fun deleteWidgetSettings(widgetId: Int) {
        widgetSettingsDao.deleteWidgetSettings(widgetId)
    }

}


