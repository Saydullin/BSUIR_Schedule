package by.devsgroup.iis.screen.employees

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import by.devsgroup.employees.ui.component.EmployeesList
import by.devsgroup.employees.ui.viewModel.EmployeeViewModel
import by.devsgroup.groups.ui.model.GroupUI

@Composable
fun EmployeesScreen(
    employeeViewModel: EmployeeViewModel
) {
    var employeeVisible by remember { mutableStateOf(false) }
    var selectedEmployee by remember { mutableStateOf<GroupUI?>(null) }

    AnimatedVisibility(
        visible = employeeVisible,
        enter = slideInVertically(
            initialOffsetY = { -40 },
            animationSpec = tween(durationMillis = 300)
        ) + fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = slideOutVertically(
            targetOffsetY = { -40 },
            animationSpec = tween(durationMillis = 300)
        ) + fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        EmployeesList(
            employeeViewModel = employeeViewModel,
        )
    }

}