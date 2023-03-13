package com.bsuir.bsuirschedule.presentation.widgets

import android.content.Context
import android.os.PowerManager

abstract class WakeLocker {

    companion object {
        private var wakeLock: PowerManager.WakeLock? = null

        fun acquire(context: Context) {
            if (wakeLock != null) {
                if (wakeLock?.isHeld == true) {
                    wakeLock?.release()
                }
            }

            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ON_AFTER_RELEASE, "WIDGET: Wake lock acquired!")
            wakeLock?.acquire(5000)
        }

        fun release() {
            wakeLock?.release()
        }
    }

}


