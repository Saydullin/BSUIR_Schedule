package com.bsuir.groups.di

import com.bsuir.domain.repository.groups.GroupDatabaseRepository
import com.bsuir.domain.repository.groups.GroupServerRepository
import com.bsuir.groups.repository.GroupDatabaseRepositoryImpl
import com.bsuir.groups.repository.GroupServerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGroupDatabaseRepository(
        groupDatabaseRepository: GroupDatabaseRepositoryImpl
    ): GroupDatabaseRepository

    @Binds
    @Singleton
    abstract fun bindGroupServerRepository(
        groupServerRepository: GroupServerRepositoryImpl
    ): GroupServerRepository

}