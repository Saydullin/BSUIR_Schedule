package by.devsgroup.employees.data.di

import by.devsgroup.domain.config.Config
import by.devsgroup.employees.data.server.service.EmployeesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EmployeesRetrofitModule {

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
    ): EmployeesService {
        return retrofit.create(EmployeesService::class.java)
    }

}