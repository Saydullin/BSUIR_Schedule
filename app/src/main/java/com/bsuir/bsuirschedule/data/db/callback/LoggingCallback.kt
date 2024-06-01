package com.bsuir.bsuirschedule.data.db.callback

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.io.File

class LoggingCallback(
    private val context: Context
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        Log.e("sady", "DATABASE CREATED v: ${db.version}")
    }

    override fun onOpen(db: SupportSQLiteDatabase) {

        Log.e("sady", "DATABASE OPENED")
        val exportDir = File(context.getExternalFilesDir(null), "AppDB.db")
        val currentDBPath = context.getDatabasePath("AppDB").absolutePath
        try {
            if (exportDir.exists()) {
                exportDir.delete()
            }
            val file = File(currentDBPath)
            file.copyTo(exportDir)
            Log.d("sady", "database exported to: ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e("sady", "error while export db $e")
            e.printStackTrace()
        }

        super.onOpen(db)
    }

}


