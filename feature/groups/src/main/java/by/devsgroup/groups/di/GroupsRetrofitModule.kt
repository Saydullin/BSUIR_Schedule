package by.devsgroup.groups.di

import by.devsgroup.groups.server.service.GroupsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GroupsRetrofitModule {

    @Provides
    @Singleton
    fun provideStudentGroupsApi(
        retrofit: Retrofit
    ): GroupsService {
        return retrofit.create(GroupsService::class.java)
    }

}


