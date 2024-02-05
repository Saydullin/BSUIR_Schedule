package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.repository.SharedPrefsRepository

class SharedPrefsUseCase(
    private val sharedPrefsRepository: SharedPrefsRepository
) {

    fun isFirstTime(): Boolean {
        return sharedPrefsRepository.isFirstTime()
    }

    fun setFirstTime(isFirst: Boolean) {
        sharedPrefsRepository.setFirstTime(isFirst)
    }

    fun setAutoUpdate(isAutoUpdate: Boolean) {
        sharedPrefsRepository.setAutoUpdate(isAutoUpdate)
    }

    fun isAutoUpdate(): Boolean {
        return sharedPrefsRepository.isAutoUpdate()
    }

    fun getPrevVersion(): Int {
        return sharedPrefsRepository.getPrevVersion()
    }

    fun setPrevVersion(prevVersion: Int) {
        return sharedPrefsRepository.setPrevVersion(prevVersion)
    }

    fun getActiveScheduleId(): Int {
        return sharedPrefsRepository.getActiveScheduleId()
    }

    fun setActiveScheduleId(scheduleId: Int) {
        sharedPrefsRepository.setActiveScheduleId(scheduleId)
    }

    fun getScheduleCounter(): Int {
        return sharedPrefsRepository.getScheduleUpdateCounter()
    }

    fun getThemeType(): Int {
        return sharedPrefsRepository.getThemeType()
    }

    fun setThemeType(themeType: Int) {
        return sharedPrefsRepository.setTheme(themeType)
    }

    fun setScheduleCounter(counter: Int) {
        sharedPrefsRepository.setScheduleUpdateCounter(counter)
    }

    fun isNotificationsEnabled(): Boolean {
        return sharedPrefsRepository.isNotificationsEnabled()
    }

    fun setNotificationsEnabled(isNotificationsEnabled: Boolean) {
        return sharedPrefsRepository.setNotificationsEnabled(isNotificationsEnabled)
    }

    fun getDefaultScheduleTitle(): String? {
        return sharedPrefsRepository.getDefaultScheduleTitle()
    }

    fun setDefaultScheduleTitle(scheduleTitle: String) {
        sharedPrefsRepository.setDefaultScheduleTitle(scheduleTitle)
    }

    fun getScheduleAutoUpdateDate(): String {
        return sharedPrefsRepository.getScheduleAutoUpdateDate()
    }

    fun setScheduleAutoUpdateDate(autoUpdateDate: String) {
        sharedPrefsRepository.setScheduleAutoUpdateDate(autoUpdateDate)
    }

}