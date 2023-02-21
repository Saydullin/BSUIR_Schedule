package com.bsuir.bsuirschedule.domain.usecase.schedule

import com.bsuir.bsuirschedule.domain.models.WidgetSettings
import com.bsuir.bsuirschedule.domain.repository.WidgetSettingsRepository

class WidgetManagerUseCase (
    private val widgetSettingsRepository: WidgetSettingsRepository
) {

    fun getWidgetSettings(widgetId: Int): WidgetSettings? {
        return widgetSettingsRepository.getWidgetSettings(widgetId)
    }

    fun saveWidgetSettings(widgetSettings: WidgetSettings) {
        widgetSettingsRepository.saveWidgetSettings(widgetSettings)
    }

    fun deleteWidgetSettings(widgetId: Int) {
        widgetSettingsRepository.deleteWidgetSettings(widgetId)
    }

}


