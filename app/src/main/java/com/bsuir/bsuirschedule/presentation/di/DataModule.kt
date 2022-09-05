package com.bsuir.bsuirschedule.presentation.di

import android.app.Application
import androidx.room.Room
import com.bsuir.bsuirschedule.data.db.AppDatabase
import com.bsuir.bsuirschedule.data.db.converters.*
import com.bsuir.bsuirschedule.data.repository.*
import com.bsuir.bsuirschedule.domain.repository.*
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {

    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "AppDB"
        ).addTypeConverter(ScheduleDaysConverter())
            .addTypeConverter(DepartmentConverter())
            .addTypeConverter(IntListConverter())
            .addTypeConverter(StrListConverter())
            .addTypeConverter(ScheduleSubjectsConverter())
            .build()
    }

    fun groupDao(db: AppDatabase) = db.groupDao()

    fun specialityDao(db: AppDatabase) = db.specialityDao()

    fun facultyDao(db: AppDatabase) = db.facultyDao()

    fun departmentDao(db: AppDatabase) = db.departmentDao()

    fun groupScheduleDao(db: AppDatabase) = db.groupScheduleDao()

    fun employeeDao(db: AppDatabase) = db.employeeDao()

    fun savedScheduleDao(db: AppDatabase) = db.savedScheduleDao()

    fun currentWeekDao(db: AppDatabase) = db.currentWeekDao()

    single { groupDao(get()) }

    single { specialityDao(get()) }

    single { facultyDao(get()) }

    single { departmentDao(get()) }

    single { groupScheduleDao(get()) }

    single { employeeDao(get()) }

    single { savedScheduleDao(get()) }

    single { currentWeekDao(get()) }

    single {
        provideDatabase(androidApplication())
    }

    single<SharedPrefsRepository> {
        SharedPrefsRepositoryImpl(get())
    }

    single<CurrentWeekRepository> {
        CurrentWeekRepositoryImpl(get())
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

    single<DepartmentRepository> {
        DepartmentRepositoryImpl(get())
    }

}


