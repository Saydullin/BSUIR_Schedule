package com.example.bsuirschedule.presentation.di

import com.example.bsuirschedule.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {

    factory {
        GroupScheduleUseCase(
            scheduleRepository = get(),
            groupItemsRepository = get(),
            fullScheduleUseCase = get()
        )
    }

    factory {
        EmployeeScheduleUseCase(
            scheduleRepository = get(),
            employeeItemsRepository = get(),
            groupItemsRepository = get(),
            fullScheduleUseCase = get()
        )
    }

    factory {
        FullScheduleUseCase()
    }

    factory {
        FullExamsScheduleUseCase()
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


