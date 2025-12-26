package by.devsgroup.groups.di

import by.devsgroup.domain.repository.groups.GroupDatabaseRepository
import by.devsgroup.domain.repository.groups.GroupServerRepository
import by.devsgroup.groups.repository.GroupDatabaseRepositoryImpl
import by.devsgroup.groups.repository.GroupServerRepositoryImpl
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