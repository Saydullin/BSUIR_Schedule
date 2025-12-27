package by.devsgroup.employees.di

import by.devsgroup.employees.server.service.EmployeesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EmployeesRetrofitModule {

    @Provides
    @Singleton
    fun provideStudentGroupsApi(
        retrofit: Retrofit
    ): EmployeesService {
        return retrofit.create(EmployeesService::class.java)
    }

}