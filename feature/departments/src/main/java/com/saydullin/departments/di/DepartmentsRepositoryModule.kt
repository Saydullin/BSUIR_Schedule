package com.saydullin.departments.di

import by.devsgroup.domain.repository.department.DepartmentServerRepository
import com.saydullin.departments.repository.DepartmentServerRepositoryImpl
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
        departmentServerRepository: DepartmentServerRepository
    ): DepartmentServerRepositoryImpl

}