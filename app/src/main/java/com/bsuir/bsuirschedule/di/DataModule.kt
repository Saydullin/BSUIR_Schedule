package com.bsuir.bsuirschedule.di

import android.app.Application
import androidx.room.Room
import com.bsuir.bsuirschedule.data.db.AppDatabase
import com.bsuir.bsuirschedule.data.db.callback.LoggingCallback
import com.bsuir.bsuirschedule.data.db.converters.*
import com.bsuir.bsuirschedule.data.db.migration.MIGRATION_10_11
import com.bsuir.bsuirschedule.data.db.migration.MIGRATION_11_12
import com.bsuir.bsuirschedule.data.db.migration.MIGRATION_12_13
import com.bsuir.bsuirschedule.data.repository.*
import com.bsuir.bsuirschedule.domain.repository.*
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {

    fun provideDatabase(application: Application): AppDatabase   {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "AppDB"
        )
            .createFromAsset("bsuirHolidays.db")
            .addMigrations(MIGRATION_10_11)
            .addMigrations(MIGRATION_11_12)
            .addMigrations(MIGRATION_12_13)
            .addCallback(LoggingCallback(application))
            .addTypeConverter(DepartmentConverter())
            .addTypeConverter(IntListConverter())
            .addTypeConverter(StrListConverter())
            .addTypeConverter(ScheduleSettingsConverter())
            .addTypeConverter(ScheduleDayListConverter())
            .addTypeConverter(ScheduleSubjectsListConverter())
            .build()
    }

    fun groupDao(db: AppDatabase) = db.groupDao()

    fun specialityDao(db: AppDatabase) = db.specialityDao()

    fun holidayDao(db: AppDatabase) = db.holidayDao()

    fun facultyDao(db: AppDatabase) = db.facultyDao()

    fun departmentDao(db: AppDatabase) = db.departmentDao()

    fun scheduleDao(db: AppDatabase) = db.scheduleDao()

    fun employeeDao(db: AppDatabase) = db.employeeDao()

    fun savedScheduleDao(db: AppDatabase) = db.savedScheduleDao()

    fun currentWeekDao(db: AppDatabase) = db.currentWeekDao()

    fun widgetSettingsDao(db: AppDatabase) = db.widgetSettingsDao()

    single { groupDao(get()) }

    single { specialityDao(get()) }

    single { facultyDao(get()) }

    single { departmentDao(get()) }

    single { scheduleDao(get()) }

    single { holidayDao(get()) }

    single { employeeDao(get()) }

    single { savedScheduleDao(get()) }

    single { currentWeekDao(get()) }

    single { widgetSettingsDao(get()) }

    single {
        provideDatabase(androidApplication())
    }

    single<SharedPrefsRepository> {
        SharedPrefsRepositoryImpl(get())
    }

    single<CurrentWeekRepository> {
        CurrentWeekRepositoryImpl(get())
    }

    single<HolidayRepository> {
        HolidayRepositoryImpl(get())
    }

    single<ScheduleRepository> {
        ScheduleRepositoryImpl(get())
    }

    single<GroupItemsRepository> {
        GroupItemsRepositoryImpl(get())
    }

    single<SavedScheduleRepository> {
        SavedScheduleRepositoryImpl(get())
    }

    single<EmployeeItemsRepository> {
        EmployeeItemsRepositoryImpl(get())
    }

    single<FacultyRepository> {
        FacultyRepositoryImpl(get())
    }

    single<SpecialityRepository> {
        SpecialityRepositoryImpl(get())
    }

    single<WidgetSettingsRepository> {
        WidgetSettingsRepositoryImpl(get())
    }

    single<DepartmentRepository> {
        DepartmentRepositoryImpl(get())
    }

}


