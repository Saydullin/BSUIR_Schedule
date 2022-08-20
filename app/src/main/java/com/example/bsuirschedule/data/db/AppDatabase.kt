package com.example.bsuirschedule.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bsuirschedule.data.db.converters.*
import com.example.bsuirschedule.data.db.dao.*
import com.example.bsuirschedule.data.db.entities.*

@Database(
    entities = [
        GroupTable::class,
        EmployeeTable::class,
        ActiveGroupTable::class,
        GroupScheduleTable::class,
        SavedScheduleTable::class,
        FacultyTable::class,
        SpecialityTable::class,
        DepartmentTable::class,
        CurrentWeekTable::class
    ],
    version = 1
)
@TypeConverters(
    ScheduleDaysConverter::class,
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

    abstract fun groupScheduleDao(): GroupScheduleDao

    abstract fun savedScheduleDao(): SavedScheduleDao

    abstract fun currentWeekDao(): CurrentWeekDao

}


