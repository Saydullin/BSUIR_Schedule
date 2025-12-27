package com.saydullin.faculty.di

import com.saydullin.faculty.server.service.FacultyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FacultyRetrofitModule {

    @Provides
    @Singleton
    fun provideStudentGroupsApi(
        retrofit: Retrofit
    ): FacultyService {
        return retrofit.create(FacultyService::class.java)
    }

}