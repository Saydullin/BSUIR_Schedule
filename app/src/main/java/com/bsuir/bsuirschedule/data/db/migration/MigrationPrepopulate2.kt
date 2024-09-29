package com.bsuir.bsuirschedule.data.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_12_13 = object : Migration(12, 13) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS `HolidaysTable`;")

        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `HolidaysTable` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`date` INTEGER NOT NULL, " +
                    "`title` TEXT NOT NULL" +
                    ");"
        )

        database.execSQL(
            "INSERT INTO `HolidaysTable` (`id`, `date`, `title`) " +
                    "VALUES " +
                    "(1, 31557600000, 'Новый год')," +
                    "(2, 31644000000, 'Новый год')," +
                    "(3, 5680800000, 'Рождество Христово (православное Рождество)')," +
                    "(4, 6793200000, 'День женщин')," +
                    "(5, 10368000000, 'Праздник труда')," +
                    "(6, 11048400000, 'День Победы')," +
                    "(8, 15800400000, 'День Независимости Республики Беларусь')," +
                    "(9, 26773200000, 'День Октябрьской революции')," +
                    "(10, 30920400000, 'Рождество Христово (католическое Рождество)');"
        )
    }

}


