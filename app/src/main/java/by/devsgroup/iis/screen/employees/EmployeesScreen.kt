package by.devsgroup.iis.screen.employees

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.devsgroup.employees.ui.component.EmployeesList
import by.devsgroup.employees.ui.model.EmployeeUI
import by.devsgroup.employees.ui.viewModel.EmployeeViewModel
import by.devsgroup.schedule.ui.model.PreviewScheduleType
import by.devsgroup.schedule.ui.viewModel.PreviewScheduleViewModel
import by.devsgroup.schedule.ui.viewModel.ScheduleViewModel
import by.devsgroup.ui_kit.dialog.DialogModal
import by.devsgroup.ui_kit.search.TextSearch

@Composable
fun EmployeesScreen(
    employeeViewModel: EmployeeViewModel,
    scheduleViewModel: ScheduleViewModel,
    previewScheduleViewModel: PreviewScheduleViewModel,
) {
    val focusManager = LocalFocusManager.current

    val previewScheduleList = previewScheduleViewModel.previewSchedules.collectAsStateWithLifecycle()

    val existingEmployeeUrls: List<String> = previewScheduleList.value
        ?.filterIsInstance<PreviewScheduleType.Employee>()
        ?.mapNotNull { it.urlId } ?: listOf()

    var search by remember { mutableStateOf("") }

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

    Column(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            },
    ) {
        TextSearch(
            modifier = Modifier
                .padding(16.dp),
            search = search,
            onSearchChange = {
                search = it

                employeeViewModel.setSearch(it)
            }
        )
        EmployeesList(
            employeeViewModel = employeeViewModel,
            existingEmployeeUrls = existingEmployeeUrls,
            onClick = {
                selectedEmployee = it
            }
        )
    }

}