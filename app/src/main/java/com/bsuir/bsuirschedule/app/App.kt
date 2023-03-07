package com.bsuir.bsuirschedule.app

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import com.bsuir.bsuirschedule.data.repository.SharedPrefsRepositoryImpl
import com.bsuir.bsuirschedule.data.repository.ThemeType
import com.bsuir.bsuirschedule.presentation.di.appModule
import com.bsuir.bsuirschedule.presentation.di.dataModule
import com.bsuir.bsuirschedule.presentation.di.domainModule
import com.bsuir.bsuirschedule.receiver.ScheduleUpdater
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.text.SimpleDateFormat
import java.util.*

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val prefs = SharedPrefsRepositoryImpl(this)

        when (prefs.getThemeType()) {
            ThemeType.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            ThemeType.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            ThemeType.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val todayDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val todayDate = todayDateFormat.format(Date().time)
        val scheduleAutoUpdateDate = prefs.getScheduleAutoUpdateDate()
        if (scheduleAutoUpdateDate != "" && prefs.getScheduleAutoUpdateDate() != todayDate) {
            val intent = Intent(this, ScheduleUpdater::class.java)
            intent.action = "com.bsuir.bsuirschedule.action.scheduleUpdater"
            sendBroadcast(intent)
            prefs.setScheduleAutoUpdateDate(todayDate)
        }

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(appModule, dataModule, domainModule))
        }
    }

}


