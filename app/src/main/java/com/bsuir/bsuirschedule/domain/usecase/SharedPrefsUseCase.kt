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

    fun getActiveScheduleId(): Int {
        return sharedPrefsRepository.getActiveScheduleId()
    }

    fun setActiveScheduleId(scheduleId: Int) {
        sharedPrefsRepository.setActiveScheduleId(scheduleId)
    }

    fun getScheduleCounter(): Int {
        return sharedPrefsRepository.getScheduleUpdateCounter()
    }

    fun setScheduleCounter(counter: Int) {
        sharedPrefsRepository.setScheduleUpdateCounter(counter)
    }

}