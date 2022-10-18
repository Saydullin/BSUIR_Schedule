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
        ActiveGroupTable::class,
        ScheduleSettingsTable::class,
        ScheduleAlarmTable::class,
        ScheduleTable::class,
        SavedScheduleTable::class,
        FacultyTable::class,
        SpecialityTable::class,
        DepartmentTable::class,
        CurrentWeekTable::class
    ],
    version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 4)
    ]
)
@TypeConverters(
    ScheduleDayListConverter::class,
    EmployeeScheduleConverter::class,
    ScheduleSubjectsConverter::class,
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


