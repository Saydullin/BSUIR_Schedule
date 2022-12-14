package com.bsuir.bsuirschedule.presentation.di

import com.bsuir.bsuirschedule.domain.usecase.*
import com.bsuir.bsuirschedule.domain.usecase.schedule.*
import org.koin.dsl.module

val domainModule = module {

    factory {
        GetScheduleUseCase(
            groupItemsRepository = get(),
            currentWeekUseCase = get(),
            employeeItemsRepository = get(),
            scheduleRepository = get()
        )
    }

    factory {
        GetScheduleLastUpdateUseCase(
            scheduleRepository = get()
        )
    }

    factory {
        ScheduleSubjectUseCase(
            scheduleRepository = get()
        )
    }

    factory {
        SaveScheduleLastUpdateDateUseCase(
            scheduleRepository = get()
        )
    }

    factory {
        UpdateScheduleSettingsUseCase(
            scheduleRepository = get()
        )
    }

    factory {
        DeleteScheduleUseCase(
            scheduleRepository = get()
        )
    }

    factory {
        SaveScheduleUseCase(
            scheduleRepository = get()
        )
    }

    factory {
        GetGroupItemsUseCase(
            groupItemsRepository = get(),
            specialityRepository = get(),
            facultyRepository = get()
        )
    }

    factory {
        SharedPrefsUseCase(sharedPrefsRepository = get())
    }

    factory {
        GetCurrentWeekUseCase(currentWeekRepository = get())
    }

    factory {
        GetEmployeeItemsUseCase(
            employeeItemsRepository = get(),
            departmentRepository = get()
        )
    }

    factory {
        GetSavedScheduleUseCase(savedScheduleRepository = get())
    }

    factory {
        GetFacultyUseCase(facultyRepository = get())
    }

    factory {
        GetSpecialityUseCase(specialityRepository = get())
    }

    factory {
        GetDepartmentUseCase(departmentRepository = get())
    }

}


