package com.bsuir.bsuirschedule.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bsuir.bsuirschedule.presentation.widgets.WakeLocker
import com.bsuir.bsuirschedule.receiver.AlarmReceiver

class AlarmUpdateService : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        WakeLocker.acquire(context)

        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent.action = "com.bsuir.bsuirschedule.action.ALARM_UPDATE"
        context.sendBroadcast(alarmIntent)

        WakeLocker.release()
    }

}