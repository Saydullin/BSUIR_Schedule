package by.devsgroup.iis.screen.groupsAndEmployees

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import by.devsgroup.employees.ui.viewModel.EmployeeViewModel
import by.devsgroup.groups.ui.viewModel.GroupViewModel
import by.devsgroup.iis.navController.navigateFinal
import by.devsgroup.iis.navigation.ScreenNav

@Composable
fun GroupsAndEmployeesScreen(
    groupViewModel: GroupViewModel,
    employeeViewModel: EmployeeViewModel,
) {
    val navController = rememberNavController()

    val tabs = listOf(
        "Группы" to ScreenNav.AllGroups.route,
        "Преподаватели" to ScreenNav.AllEmployees.route,
    )

    var selectedTabIndex by remember { mutableStateOf(0) }

    Column {
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
        ) {
            tabs.forEachIndexed { index, (title, route) ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        navController.navigateFinal(route)
                    },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                )
            }
        }

        GroupsAndEmployeesNavHost(
            employeeViewModel = employeeViewModel,
            groupViewModel = groupViewModel,
            navController = navController,
        )
    }

}


