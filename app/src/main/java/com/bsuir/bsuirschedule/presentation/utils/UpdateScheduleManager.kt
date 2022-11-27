package com.bsuir.bsuirschedule.presentation.utils

import android.content.Context
import com.bsuir.bsuirschedule.domain.usecase.SharedPrefsUseCase
import com.bsuir.bsuirschedule.service.ScheduleService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class UpdateScheduleManager(
    private val context: Context
) : KoinComponent {

    private val sharedPrefsUseCase: SharedPrefsUseCase by inject()

    fun execute() {
//        if (!sharedPrefsUseCase.isAutoUpdate()) {
//            val scheduleAlarm = ScheduleService(context)
//            scheduleAlarm.setRepeatAlarm()
//            sharedPrefsUseCase.setAutoUpdate(true)
//        }
        val scheduleAlarm = ScheduleService(context)
        scheduleAlarm.setRepeatAlarm()
    }

}