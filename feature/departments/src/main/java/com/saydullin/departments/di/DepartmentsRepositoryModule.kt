package com.saydullin.departments.di

import com.bsuir.domain.repository.department.DepartmentsDatabaseRepository
import com.bsuir.domain.repository.department.DepartmentsServerRepository
import com.bsuir.domain.repository.department.EmployeeDepartmentsDatabaseRepository
import com.saydullin.departments.repository.DepartmentsDatabaseRepositoryImpl
import com.saydullin.departments.repository.DepartmentsServerRepositoryImpl
import com.saydullin.departments.repository.EmployeeDepartmentsDatabaseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DepartmentsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDepartmentServerRepository(
        departmentsServerRepository: DepartmentsServerRepositoryImpl
    ): DepartmentsServerRepository

    @Binds
    @Singleton
    abstract fun bindDepartmentsDatabaseRepository(
        departmentsDatabaseRepository: DepartmentsDatabaseRepositoryImpl
    ): DepartmentsDatabaseRepository

    @Binds
    @Singleton
    abstract fun bindEmployeeDepartmentsDatabaseRepository(
        employeeDepartmentsDatabaseRepository: EmployeeDepartmentsDatabaseRepositoryImpl
    ): EmployeeDepartmentsDatabaseRepository

}