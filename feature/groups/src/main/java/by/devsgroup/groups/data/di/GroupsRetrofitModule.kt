package by.devsgroup.groups.data.di

import by.devsgroup.domain.config.Config
import by.devsgroup.groups.data.server.service.GroupsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GroupsRetrofitModule {

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
    ): GroupsService {
        return retrofit.create(GroupsService::class.java)
    }

}