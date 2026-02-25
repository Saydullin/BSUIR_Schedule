package com.bsuir.week.di

import com.bsuir.domain.repository.week.WeekDatabaseRepository
import com.bsuir.domain.repository.week.WeekServerRepository
import com.bsuir.week.repository.WeekDatabaseRepositoryImpl
import com.bsuir.week.repository.WeekServerRepositoryImpl
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


