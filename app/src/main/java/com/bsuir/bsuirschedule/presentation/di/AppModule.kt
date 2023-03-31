package com.bsuir.bsuirschedule.presentation.di

import com.bsuir.bsuirschedule.domain.utils.ScheduleUpdateManager
import com.bsuir.bsuirschedule.presentation.viewModels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        ScheduleViewModel(
            sharedPrefsUseCase = get(),
            scheduleSubjectUseCase = get(),
            getCurrentWeekUseCase = get(),
            getScheduleUseCase = get(),
            saveScheduleUseCase = get(),
            deleteScheduleUseCase = get(),
            updateScheduleSettingsUseCase = get(),
            savedScheduleUseCase = get(),
            addScheduleSubjectUseCase = get(),
        )
    }

    single {
        ScheduleUpdateManager(
            getScheduleLastUpdateUseCase = get(),
            getSavedScheduleUseCase = get(),
            getScheduleUseCase = get(),
            saveSavedScheduleUseCase = get(),
            saveScheduleLastUpdateDateUseCase = get(),
        )
    }

    viewModel {
        CurrentWeekViewModel(
            getCurrentWeekUseCase = get()
        )
    }

    viewModel {
        GroupItemsViewModel(
            getGroupItemsUseCase = get(),
            getFacultyUseCase = get(),
            getSpecialityUseCase = get()
        )
    }

    viewModel {
        EmployeeItemsViewModel(
            getEmployeeItemsUseCase = get(),
            getDepartmentUseCase = get()
        )
    }

    viewModel {
        SavedSchedulesViewModel(
            getSavedScheduleUseCase = get(),
            sharedPrefsUseCase = get()
        )
    }

}


