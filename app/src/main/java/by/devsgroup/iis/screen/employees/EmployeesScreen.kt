package by.devsgroup.iis.screen.employees

import androidx.compose.runtime.Composable
import by.devsgroup.employees.ui.component.EmployeesList
import by.devsgroup.employees.ui.viewModel.EmployeeViewModel

@Composable
fun EmployeesScreen(
    employeeViewModel: EmployeeViewModel
) {

    EmployeesList(
        employeeViewModel = employeeViewModel,
    )

}