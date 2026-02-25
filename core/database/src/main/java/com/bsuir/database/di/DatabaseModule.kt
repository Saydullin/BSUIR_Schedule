package com.bsuir.database.di

import android.content.Context
import androidx.room.Room
import com.bsuir.database.AppDatabase
import com.bsuir.database.departments.dao.DepartmentDao
import com.bsuir.database.departments.dao.EmployeeDepartmentDao
import com.bsuir.database.employees.dao.EmployeeDao
import com.bsuir.database.faculty.dao.FacultyDao
import com.bsuir.database.groups.dao.GroupDao
import com.bsuir.database.schedule.dao.ScheduleDao
import com.bsuir.database.schedule.dao.ScheduleDayDao
import com.bsuir.database.schedule.dao.ScheduleEmployeeDao
import com.bsuir.database.schedule.dao.ScheduleGroupDao
import com.bsuir.database.schedule.dao.ScheduleLessonDao
import com.bsuir.database.schedule.dao.ScheduleLessonEmployeeDao
import com.bsuir.database.schedule.dao.ScheduleLessonGroupDao
import com.bsuir.database.specialty.dao.SpecialtyDao
import com.bsuir.database.week.dao.WeekDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideGroupDao(
        appDatabase: AppDatabase
    ): GroupDao {
        return appDatabase.getGroupDao()
    }

    @Provides
    @Singleton
    fun provideWeekDao(
        appDatabase: AppDatabase
    ): WeekDao {
        return appDatabase.getWeekDao()
    }

    @Provides
    @Singleton
    fun provideEmployeeDao(
        appDatabase: AppDatabase
    ): EmployeeDao {
        return appDatabase.getEmployeeDao()
    }

    @Provides
    @Singleton
    fun provideSpecialtyDao(
        appDatabase: AppDatabase
    ): SpecialtyDao {
        return appDatabase.getSpecialtyDao()
    }

    @Provides
    @Singleton
    fun provideFacultyDao(
        appDatabase: AppDatabase
    ): FacultyDao {
        return appDatabase.getFacultyDao()
    }

    @Provides
    @Singleton
    fun provideDepartmentDao(
        appDatabase: AppDatabase
    ): DepartmentDao {
        return appDatabase.getDepartmentDao()
    }

    @Provides
    @Singleton
    fun provideScheduleLessonDao(
        appDatabase: AppDatabase
    ): ScheduleLessonDao {
        return appDatabase.getScheduleLessonDao()
    }

    @Provides
    @Singleton
    fun provideEmployeeDepartmentDao(
        appDatabase: AppDatabase
    ): EmployeeDepartmentDao {
        return appDatabase.getEmployeeDepartmentDao()
    }

    @Provides
    @Singleton
    fun provideScheduleLessonEmployeeDao(
        appDatabase: AppDatabase
    ): ScheduleLessonEmployeeDao {
        return appDatabase.getScheduleLessonEmployeeDao()
    }

    @Provides
    @Singleton
    fun provideScheduleLessonGroupDao(
        appDatabase: AppDatabase
    ): ScheduleLessonGroupDao {
        return appDatabase.getScheduleLessonGroupDao()
    }

    @Provides
    @Singleton
    fun provideScheduleDayDao(
        appDatabase: AppDatabase
    ): ScheduleDayDao {
        return appDatabase.getScheduleDayDao()
    }

    @Provides
    @Singleton
    fun provideScheduleGroupDao(
        appDatabase: AppDatabase
    ): ScheduleGroupDao {
        return appDatabase.getScheduleGroupDao()
    }

    @Provides
    @Singleton
    fun provideScheduleEmployeeDao(
        appDatabase: AppDatabase
    ): ScheduleEmployeeDao {
        return appDatabase.getScheduleEmployeeDao()
    }

    @Provides
    @Singleton
    fun provideScheduleDao(
        appDatabase: AppDatabase
    ): ScheduleDao {
        return appDatabase.getScheduleDao()
    }

}


