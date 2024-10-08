package com.bsuir.bsuirschedule.di

import com.bsuir.bsuirschedule.domain.utils.ScheduleUpdateManager
import com.bsuir.bsuirschedule.presentation.viewModels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        ScheduleViewModel(
            sharedPrefsUseCase = get(),
            ignoreSubjectUseCase = get(),
            deleteSubjectUseCase = get(),
            editSubjectUseCase = get(),
            getCurrentWeekUseCase = get(),
            getScheduleUseCase = get(),
            saveScheduleUseCase = get(),
            deleteScheduleUseCase = get(),
            updateScheduleSettingsUseCase = get(),
            savedScheduleUseCase = get(),
            addScheduleSubjectUseCase = get(),
            addSubjectUseCase = get()
        )
    }

    viewModel {
        ScheduleUpdatedHistoryViewModel(
            getUpdatedScheduleHistoryUseCase = get()
        )
    }

    single {
        ScheduleUpdateManager(
            getScheduleUseCase = get(),
            getSavedScheduleUseCase = get(),
            saveScheduleUseCase = get()
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


