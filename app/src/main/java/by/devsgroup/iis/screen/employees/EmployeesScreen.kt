package by.devsgroup.iis.screen.employees

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import by.devsgroup.employees.ui.component.EmployeesList
import by.devsgroup.employees.ui.model.EmployeeUI
import by.devsgroup.employees.ui.viewModel.EmployeeViewModel
import by.devsgroup.schedule.ui.viewModel.ScheduleViewModel
import by.devsgroup.ui_kit.dialog.DialogModal

@Composable
fun EmployeesScreen(
    employeeViewModel: EmployeeViewModel,
    scheduleViewModel: ScheduleViewModel,
) {
    var selectedEmployee by remember { mutableStateOf<EmployeeUI?>(null) }

    selectedEmployee?.let { group ->
        DialogModal(
            title = "Загрузить расписание?",
            description = "Расписание преподавателя ${selectedEmployee?.getFullName()} можно будет смотреть оффлайн",
            positiveButtonText = "Загрузить",
            negativeButtonText = "Отмена",
            onSkip = { selectedEmployee = null },
            onDismiss = { selectedEmployee = null },
            onConfirm = {
                selectedEmployee?.urlId?.let { urlId ->
                    scheduleViewModel.loadEmployeeSchedule(urlId)
                    selectedEmployee = null
                }
            }
        )
    }

    EmployeesList(
        employeeViewModel = employeeViewModel,
        onClick = {
            selectedEmployee = it
        }
    )

}