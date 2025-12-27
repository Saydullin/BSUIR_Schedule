package com.saydullin.specialty.di

import by.devsgroup.domain.repository.specialty.SpecialtyServerRepository
import com.saydullin.specialty.repository.SpecialtyServerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SpecialityRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSpecialtyServerRepository(
        specialtyServerRepository: SpecialtyServerRepositoryImpl
    ): SpecialtyServerRepository

}