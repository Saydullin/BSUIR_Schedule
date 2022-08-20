package com.example.bsuirschedule.presentation.di

import com.example.bsuirschedule.presentation.viewModels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        InitialDataViewModel(
            getFacultyUseCase = get(),
            getSpecialityUseCase = get(),
            getDepartmentUseCase = get()
        )
    }

    viewModel {
        GroupScheduleViewModel(
            groupScheduleUseCase = get(),
            employeeScheduleUseCase = get(),
            sharedPrefsUseCase = get(),
            examsScheduleUseCase = get()
        )
    }

    viewModel {
        CurrentWeekViewModel(
            getCurrentWeekUseCase = get()
        )
    }

    viewModel {
        GroupItemsViewModel(
            getGroupItemsUseCase = get()
        )
    }

    viewModel {
        EmployeeItemsViewModel(
            getEmployeeItemsUseCase = get()
        )
    }

    viewModel {
        SavedSchedulesViewModel(
            getSavedScheduleUseCase = get(),
            sharedPrefsUseCase = get()
        )
    }

}


