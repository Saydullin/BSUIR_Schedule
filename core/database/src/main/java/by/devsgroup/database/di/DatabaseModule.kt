package by.devsgroup.database.di

import android.content.Context
import androidx.room.Room
import by.devsgroup.database.AppDatabase
import by.devsgroup.database.departments.dao.DepartmentDao
import by.devsgroup.database.departments.dao.EmployeeDepartmentDao
import by.devsgroup.database.employees.dao.EmployeeDao
import by.devsgroup.database.faculty.dao.FacultyDao
import by.devsgroup.database.groups.dao.GroupDao
import by.devsgroup.database.schedule.dao.ScheduleDao
import by.devsgroup.database.schedule.dao.ScheduleLessonDao
import by.devsgroup.database.specialty.dao.SpecialtyDao
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
    fun provideScheduleDao(
        appDatabase: AppDatabase
    ): ScheduleDao {
        return appDatabase.getScheduleDao()
    }

}


