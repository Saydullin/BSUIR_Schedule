package com.bsuir.schedule.di

import com.bsuir.domain.repository.schedule.ScheduleDatabaseRepository
import com.bsuir.domain.repository.schedule.ScheduleServerRepository
import com.bsuir.schedule.repository.ScheduleDatabaseRepositoryImpl
import com.bsuir.schedule.repository.ScheduleServerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ScheduleRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindScheduleServerRepository(
        scheduleServerRepository: ScheduleServerRepositoryImpl
    ): ScheduleServerRepository

    @Binds
    @Singleton
    abstract fun bindScheduleDatabaseRepository(
        scheduleDatabaseRepository: ScheduleDatabaseRepositoryImpl
    ): ScheduleDatabaseRepository

}