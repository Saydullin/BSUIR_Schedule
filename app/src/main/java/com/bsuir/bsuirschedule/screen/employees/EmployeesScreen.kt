package com.bsuir.bsuirschedule.screen.employees

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bsuir.employees.ui.component.EmployeesList
import com.bsuir.employees.ui.model.EmployeeUI
import com.bsuir.employees.ui.viewModel.EmployeeViewModel
import com.bsuir.schedule.ui.model.PreviewScheduleType
import com.bsuir.schedule.ui.viewModel.PreviewScheduleViewModel
import com.bsuir.schedule.ui.viewModel.ScheduleViewModel
import com.bsuir.ui_kit.dialog.DialogModal
import com.bsuir.ui_kit.search.TextSearch
import coil.compose.AsyncImage

@Composable
fun EmployeesScreen(
    navController: NavController,
    employeeViewModel: EmployeeViewModel,
    scheduleViewModel: ScheduleViewModel,
    previewScheduleViewModel: PreviewScheduleViewModel,
    onRedirectToSchedule: () -> Unit,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val previewScheduleList = previewScheduleViewModel.previewSchedules.collectAsStateWithLifecycle()

    val existingEmployeeUrls: List<String> = previewScheduleList.value
        ?.filterIsInstance<PreviewScheduleType.Employee>()
        ?.mapNotNull { it.urlId } ?: listOf()

    var search by remember { mutableStateOf("") }

    var selectedEmployee by remember { mutableStateOf<EmployeeUI?>(null) }

    selectedEmployee?.let { employee ->
        val employeeUrlId = employee.urlId
        val employeeId = employee.id
        val loaded = existingEmployeeUrls.contains(employee.urlId)

        if (employeeUrlId == null || employeeId == null) {
            DialogModal(
                title = employee.getFullName(),
                description = "Сервер вернул какого-то странного преподавателя. Его не получится загрузить",
                positiveButtonText = "Понятно",
                negativeButtonText = "Какой ужас",
                onSkip = { selectedEmployee = null },
                onDismiss = { selectedEmployee = null },
                onConfirm = { selectedEmployee = null }
            )

            return@let
        }

        if (loaded) {
            DialogModal(
                title = employee.getFullName(),
                description = "Расписание уже загружено",
                positiveButtonText = "Открыть",
                negativeButtonText = "Отмена",
                icon = {
                    AsyncImage(
                        modifier = Modifier
                            .size(246.dp)
                            .clip(RoundedCornerShape(32.dp)),
                        model = employee.photoLink,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                },
                onSkip = { selectedEmployee = null },
                onDismiss = { selectedEmployee = null },
                onConfirm = {
                    selectedEmployee = null

                    scheduleViewModel.loadEmployeeSchedule(employeeUrlId)

                    scheduleViewModel.setCurrentScheduleByEmployeeUrlId(employeeId)

                    onRedirectToSchedule()
                }
            )
        } else {
            DialogModal(
                title = employee.getFullName(),
                description = "Загрузить расписание преподавателя?",
                positiveButtonText = "Загрузить",
                negativeButtonText = "Отмена",
                icon = {
                    AsyncImage(
                        modifier = Modifier
                            .size(246.dp)
                            .clip(RoundedCornerShape(32.dp)),
                        model = employee.photoLink,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                },
                onSkip = { selectedEmployee = null },
                onDismiss = { selectedEmployee = null },
                onConfirm = {
                    Toast.makeText(context, "Загрузка расписания ...", Toast.LENGTH_SHORT).show()

                    selectedEmployee?.urlId?.let { urlId ->
                        scheduleViewModel.loadEmployeeSchedule(urlId)
                        selectedEmployee = null
                    }
                }
            )
        }
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