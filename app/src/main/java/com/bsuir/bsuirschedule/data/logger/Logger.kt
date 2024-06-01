package com.bsuir.bsuirschedule.data.logger

import android.content.Context
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Logger(private val context: Context) {

    private val logFileName = "logs_debug.txt"

    fun log(message: String) {
        val logMessage = formatLogMessage(message)
        writeToFile(logMessage)
    }

    private fun formatLogMessage(message: String): String {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        return "$timestamp: $message\n"
    }

    private fun writeToFile(logMessage: String) {
        val logFile = File(context.getExternalFilesDir(null), logFileName)
        Log.e("sady", "writing logFile ${logFile.isFile} ${logFile.absolutePath}")
        try {
            logFile.appendText(logMessage)
        } catch (e: Exception) {
            Log.e("sady", "error Print ${e.printStackTrace()}")
            e.printStackTrace()
        }
    }

}


