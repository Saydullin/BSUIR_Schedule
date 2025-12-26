package com.saydullin.departments.di

import by.devsgroup.domain.config.Config
import com.saydullin.departments.data.server.service.DepartmentsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DepartmentsRetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideStudentGroupsApi(
        retrofit: Retrofit
    ): DepartmentsService {
        return retrofit.create(DepartmentsService::class.java)
    }

}