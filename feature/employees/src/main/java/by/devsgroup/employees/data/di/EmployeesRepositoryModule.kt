package by.devsgroup.employees.data.di

import by.devsgroup.domain.repository.employees.EmployeesDatabaseRepository
import by.devsgroup.domain.repository.employees.EmployeesServerRepository
import by.devsgroup.employees.repository.EmployeesDatabaseRepositoryImpl
import by.devsgroup.employees.repository.EmployeesServerRepositoryImpl
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

