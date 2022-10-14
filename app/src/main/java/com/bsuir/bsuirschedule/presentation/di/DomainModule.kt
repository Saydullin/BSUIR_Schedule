package com.bsuir.bsuirschedule.presentation.di

import com.bsuir.bsuirschedule.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {

    factory {
        GroupScheduleUseCase(
            scheduleRepository = get(),
            groupItemsRepository = get(),
            employeeItemsRepository = get(),
            currentWeekUseCase = get()
        )
    }

    factory {
        EmployeeScheduleUseCase(
            scheduleRepository = get(),
            employeeItemsRepository = get(),
            groupItemsRepository = get(),
            fullScheduleUseCase = get(),
            currentWeekUseCase = get()
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


