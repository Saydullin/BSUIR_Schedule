package com.bsuir.bsuirschedule.app

import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.work.*
import com.bsuir.bsuirschedule.BuildConfig
import com.bsuir.bsuirschedule.data.repository.SharedPrefsRepositoryImpl
import com.bsuir.bsuirschedule.presentation.activities.BaseActivity
import com.bsuir.bsuirschedule.presentation.di.appModule
import com.bsuir.bsuirschedule.presentation.di.dataModule
import com.bsuir.bsuirschedule.presentation.di.domainModule
import com.bsuir.bsuirschedule.presentation.utils.UpdateScheduleManager
import com.bsuir.bsuirschedule.service.ExpeditedWorker
import com.bsuir.bsuirschedule.service.JobSchedulerTest
import com.bsuir.bsuirschedule.service.WorkManagerService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val prefs = SharedPrefsRepositoryImpl(this)
        val lang = prefs.getLanguage()
        if (lang != null) {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(lang)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(appModule, dataModule, domainModule))
        }

//        val updateScheduleManager = UpdateScheduleManager(this)

//        updateScheduleManager.execute()
//        startSchedule()

//        startWorkSchedule()
    }

    private fun startSchedule() {
        val componentName = ComponentName(this, JobSchedulerTest::class.java)
        val jobInfo = JobInfo.Builder(123, componentName)
            .setPersisted(true)
            .setPeriodic(15 * 60 * 1000)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobInfo.setPeriodic(15 * 60 * 1000, 30 * 60 * 1000)
        }

        val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val resCode = scheduler.schedule(jobInfo.build())
        Log.e("sady", "result of code is $resCode")
    }

    private fun startWorkSchedule() {
        val saveRequest = PeriodicWorkRequestBuilder<ExpeditedWorker>(15, TimeUnit.MINUTES)
            .addTag("Sady")
            .build()

        WorkManager.getInstance(this).enqueue(saveRequest)
    }

}


