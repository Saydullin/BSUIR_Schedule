package com.bsuir.bsuirschedule.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class ScheduleUpdateWorkerInit(
    private val context: Context,
) {

    fun execute() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<ScheduleUpdateWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniquePeriodicWork(
            "scheduleUpdater",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

}


