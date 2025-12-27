package com.saydullin.departments.di

import com.saydullin.departments.server.service.DepartmentsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DepartmentsRetrofitModule {

    @Provides
    @Singleton
    fun provideStudentGroupsApi(
        retrofit: Retrofit
    ): DepartmentsService {
        return retrofit.create(DepartmentsService::class.java)
    }

}