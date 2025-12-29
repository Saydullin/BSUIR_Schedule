package by.devsgroup.schedule.di

import by.devsgroup.schedule.server.service.ScheduleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ScheduleRetrofitModule {

    @Provides
    @Singleton
    fun provideStudentGroupsApi(
        retrofit: Retrofit
    ): ScheduleService {
        return retrofit.create(ScheduleService::class.java)
    }

}


