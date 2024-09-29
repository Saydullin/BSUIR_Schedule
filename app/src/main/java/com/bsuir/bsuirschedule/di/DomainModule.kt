package com.bsuir.bsuirschedule.di

import com.bsuir.bsuirschedule.domain.usecase.*
import com.bsuir.bsuirschedule.domain.usecase.schedule.*
import org.koin.dsl.module

val domainModule = module {

    factory {
        GetScheduleUseCase(
            groupItemsRepository = get(),
            currentWeekUseCase = get(),
            scheduleRepository = get(),
            getHolidaysUseCase = get(),
        )
    }

    factory {
        GetHolidaysUseCase(
            holidayRepository = get(),
        )
    }

    factory {
        GetActualScheduleDayUseCase(
            getScheduleUseCase = get(),
            sharedPrefsUseCase = get(),
        )
    }

    factory {
        GetUpdatedScheduleHistoryUseCase()
    }

    factory {
        GetScheduleLastUpdateUseCase(
            scheduleRepository = get()
        )
    }

    factory {
        IgnoreSubjectUseCase(
            scheduleRepository = get()
        )
    }

    factory {
        EditSubjectUseCase(
            scheduleRepository = get()
        )
    }

    factory {
        DeleteSubjectUseCase(
            scheduleRepository = get()
        )
    }

    factory {
        SaveScheduleLastUpdateDateUseCase(
            scheduleRepository = get()
        )
    }

    factory {
        AddScheduleSubjectUseCase(
            getScheduleUseCase = get(),
            saveScheduleUseCase = get(),
            getCurrentWeekUseCase = get(),
            getGroupItemsUseCase = get(),
            getEmployeeItemsUseCase = get(),
        )
    }

    factory {
        AddSubjectUseCase(
            getScheduleUseCase = get(),
            saveScheduleUseCase = get(),
            getCurrentWeekUseCase = get(),
            getGroupItemsUseCase = get(),
            getEmployeeItemsUseCase = get(),
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
        WidgetManagerUseCase(
            widgetSettingsRepository = get()
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


