package com.saydullin.faculty.di

import by.devsgroup.domain.repository.faculty.FacultyServerRepository
import com.saydullin.faculty.repository.FacultyServerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FacultyRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFacultyServerRepository(
        facultyServerRepository: FacultyServerRepositoryImpl
    ): FacultyServerRepository

}