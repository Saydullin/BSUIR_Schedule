package com.bsuir.bsuirschedule.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
        CurrentWeekTable::class
    ],
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2),
        AutoMigration (from = 2, to = 3),
        AutoMigration (from = 3, to = 4),
        AutoMigration (from = 4, to = 5),
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

    abstract fun groupDao(): GroupDao

    abstract fun employeeDao(): EmployeeDao

    abstract fun facultyDao(): FacultyDao

    abstract fun specialityDao(): SpecialityDao

    abstract fun departmentDao(): DepartmentDao

    abstract fun scheduleDao(): ScheduleDao

    abstract fun savedScheduleDao(): SavedScheduleDao

    abstract fun currentWeekDao(): CurrentWeekDao

}


