package com.bsuir.bsuirschedule.presentation.di

import com.bsuir.bsuirschedule.domain.usecase.*
import com.bsuir.bsuirschedule.domain.usecase.schedule.DeleteScheduleUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.GetScheduleUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.SaveScheduleUseCase
import com.bsuir.bsuirschedule.domain.usecase.schedule.UpdateScheduleSettingsUseCase
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
        GetScheduleUseCase(
            groupItemsRepository = get(),
            currentWeekUseCase = get(),
            employeeItemsRepository = get(),
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


