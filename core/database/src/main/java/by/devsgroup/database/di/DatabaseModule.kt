package by.devsgroup.database.di

import android.content.Context
import androidx.room.Room
import by.devsgroup.database.AppDatabase
import by.devsgroup.employees.data.db.dao.EmployeeDao
import by.devsgroup.groups.data.db.dao.GroupDao
import com.saydullin.departments.data.db.dao.DepartmentDao
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
    fun provideDepartmentDao(
        appDatabase: AppDatabase
    ): DepartmentDao {
        return appDatabase.getDepartmentDao()
    }

}


