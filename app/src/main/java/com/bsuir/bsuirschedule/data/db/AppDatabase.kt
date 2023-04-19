package com.bsuir.bsuirschedule.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.bsuir.bsuirschedule.data.db.converters.*
import com.bsuir.bsuirschedule.data.db.dao.*
import com.bsuir.bsuirschedule.data.db.entities.*

@Database(
    entities = [
        GroupTable::class,
        EmployeeTable::class,
        ScheduleTable::class,
        SavedScheduleTable::class,
        FacultyTable::class,
        SpecialityTable::class,
        DepartmentTable::class,
        CurrentWeekTable::class,
        WidgetSettingsTable::class
    ],
    version = 8,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2),
        AutoMigration (from = 2, to = 3),
        AutoMigration (from = 3, to = 4),
        AutoMigration (from = 4, to = 5),
        AutoMigration (from = 5, to = 6),
        AutoMigration (from = 6, to = 7),
        AutoMigration (from = 7, to = 8, spec = AppDatabase.MigrationFrom7to8::class),
    ]
)
@TypeConverters(
    ScheduleDayListConverter::class,
    EmployeeScheduleConverter::class,
    ScheduleSubjectsListConverter::class,
    ScheduleSettingsConverter::class,
    DepartmentConverter::class,
    IntListConverter::class,
    StrListConverter::class,
)

abstract class AppDatabase : RoomDatabase() {

    @DeleteColumn(
        tableName = "ScheduleTable",
        columnName = "updateHistorySchedule"
    )
    @DeleteColumn(
        tableName = "ScheduleTable",
        columnName = "normalSchedules"
    )
    class MigrationFrom7to8 : AutoMigrationSpec

    abstract fun groupDao(): GroupDao

    abstract fun employeeDao(): EmployeeDao

    abstract fun facultyDao(): FacultyDao

    abstract fun specialityDao(): SpecialityDao

    abstract fun departmentDao(): DepartmentDao

    abstract fun scheduleDao(): ScheduleDao

    abstract fun savedScheduleDao(): SavedScheduleDao

    abstract fun currentWeekDao(): CurrentWeekDao

    abstract fun widgetSettingsDao(): WidgetSettingsDao

}


