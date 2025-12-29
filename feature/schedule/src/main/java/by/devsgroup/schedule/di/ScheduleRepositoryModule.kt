package by.devsgroup.schedule.di

import by.devsgroup.domain.repository.schedule.ScheduleServerRepository
import by.devsgroup.schedule.repository.ScheduleServerRepositoryImpl
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

}