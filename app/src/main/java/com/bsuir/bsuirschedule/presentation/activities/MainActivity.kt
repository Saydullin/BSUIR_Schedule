package com.bsuir.bsuirschedule.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bsuir.bsuirschedule.R
import com.bsuir.bsuirschedule.databinding.ActivityMainBinding
import com.bsuir.bsuirschedule.notification.ScheduleNotificationChannel
import com.bsuir.bsuirschedule.worker.ScheduleUpdateWorkerInit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val scheduleUpdateWorkerInit = ScheduleUpdateWorkerInit(this)
        scheduleUpdateWorkerInit.execute()

        val scheduleNotificationChannel = ScheduleNotificationChannel(context = this)
        scheduleNotificationChannel.create(
            channelId = getString(R.string.notification_update_channel_id),
            channelName = getString(R.string.notification_update_channel_name),
            channelDescription = getString(R.string.notification_update_channel_description)
        )

        setContentView(binding.root)
    }

}


