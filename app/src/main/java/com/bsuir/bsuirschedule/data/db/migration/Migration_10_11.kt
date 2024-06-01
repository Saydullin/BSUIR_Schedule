package com.bsuir.bsuirschedule.data.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_10_11 = object : Migration(10, 11) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE ScheduleTable ADD COLUMN previousSchedules TEXT DEFAULT ''"
        )
    }

}


