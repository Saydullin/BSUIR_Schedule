package by.devsgroup.week.di

import by.devsgroup.domain.repository.week.WeekDatabaseRepository
import by.devsgroup.domain.repository.week.WeekServerRepository
import by.devsgroup.week.repository.WeekDatabaseRepositoryImpl
import by.devsgroup.week.repository.WeekServerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WeekRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeekServerRepository(
        weekServerRepository: WeekServerRepositoryImpl
    ): WeekServerRepository

    @Binds
    @Singleton
    abstract fun bindWeekDatabaseRepository(
        weekDatabaseRepository: WeekDatabaseRepositoryImpl
    ): WeekDatabaseRepository

}


