package com.bsuir.bsuirschedule.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bsuir.bsuirschedule.worker.ScheduleUpdateWorker
import com.bsuir.bsuirschedule.worker.ScheduleUpdateWorkerInit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TestService : Service() {
    private var job: Job? = null
    private val interval = 5000L
    private var serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        Toast.makeText(this, "Service created", Toast.LENGTH_SHORT).show()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        job = serviceScope.launch {
            while(isActive) {
                showToast()
                delay(interval)
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
    }

    private fun showToast() {
        Toast.makeText(this, "Service working", Toast.LENGTH_SHORT).show()
    }

}


