package com.example.bsuirschedule.domain.usecase

import com.example.bsuirschedule.domain.repository.SharedPrefsRepository

class SharedPrefsUseCase(
    private val sharedPrefsRepository: SharedPrefsRepository
) {

    fun isFirstTime(): Boolean {
        return sharedPrefsRepository.isFirstTime()
    }

    fun setFirstTime(isFirst: Boolean) {
        sharedPrefsRepository.setFirstTime(isFirst)
    }

    fun getActiveScheduleId(): Int {
        return sharedPrefsRepository.getActiveScheduleId()
    }

    fun setActiveScheduleId(scheduleId: Int) {
        sharedPrefsRepository.setActiveScheduleId(scheduleId)
    }

}