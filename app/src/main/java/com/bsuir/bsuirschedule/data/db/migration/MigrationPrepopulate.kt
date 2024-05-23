package com.bsuir.bsuirschedule.data.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_11_12 = object : Migration(11, 12) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `HolidaysTable` (" +
                    "`id` INTEGER PRIMARY KEY NOT NULL, " +
                    "`date` INTEGER NOT NULL, " +
                    "`title` TEXT NOT NULL" +
                    ")"
        )
        database.execSQL(
            "INSERT INTO `HolidaysTable` (`id`, `date`, `title`) " +
                    "VALUES (1, 31438800000, 'Новый год')," +
                    "(2, 507600000, 'Рождество Христово')," +
                    "(3, 4568400000, 'День защитника Отечества')," +
                    "(4, 5691600000, 'Международный женский день')," +
                    "(5, 10368000000, 'Праздник труда')," +
                    "(6, 11048400000, 'День Победы')," +
                    "(7, 15800400000, 'День независимости')," +
                    "(8, 26773200000, 'День Октябрьской революции')," +
                    "(9, 30920400000, 'Рождество');"
        )
    }

}


