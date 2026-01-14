package by.devsgroup.week.di

import by.devsgroup.week.server.service.WeekService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WeekRetrofitModule {

    @Provides
    @Singleton
    fun provideWeekService(
        retrofit: Retrofit
    ): WeekService {
        return retrofit.create(WeekService::class.java)
    }

}


