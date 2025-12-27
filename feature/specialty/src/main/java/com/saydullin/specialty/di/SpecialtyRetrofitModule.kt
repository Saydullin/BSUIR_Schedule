package com.saydullin.specialty.di

import com.saydullin.specialty.server.service.SpecialtyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SpecialtyRetrofitModule {

    @Provides
    @Singleton
    fun provideStudentGroupsApi(
        retrofit: Retrofit
    ): SpecialtyService {
        return retrofit.create(SpecialtyService::class.java)
    }

}


