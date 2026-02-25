package com.bsuir.employees.di

import com.bsuir.domain.repository.employees.EmployeesDatabaseRepository
import com.bsuir.domain.repository.employees.EmployeesServerRepository
import com.bsuir.employees.repository.EmployeesDatabaseRepositoryImpl
import com.bsuir.employees.repository.EmployeesServerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EmployeesRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEmployeesServerRepository(
        employeesServerRepository: EmployeesServerRepositoryImpl
    ): EmployeesServerRepository

    @Binds
    @Singleton
    abstract fun bindEmployeesDatabaseRepository(
        employeesDatabaseRepository: EmployeesDatabaseRepositoryImpl
    ): EmployeesDatabaseRepository

}

