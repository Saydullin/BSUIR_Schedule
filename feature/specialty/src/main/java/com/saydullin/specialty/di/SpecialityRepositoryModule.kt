package com.saydullin.specialty.di

import com.bsuir.domain.repository.specialty.SpecialtyDatabaseRepository
import com.bsuir.domain.repository.specialty.SpecialtyServerRepository
import com.saydullin.specialty.repository.SpecialtyDatabaseRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindSpecialtyDatabaseRepository(
        specialtyServerRepository: SpecialtyDatabaseRepositoryImpl
    ): SpecialtyDatabaseRepository

}