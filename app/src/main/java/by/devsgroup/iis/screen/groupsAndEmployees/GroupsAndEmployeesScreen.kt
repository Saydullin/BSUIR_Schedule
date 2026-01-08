package by.devsgroup.iis.screen.groupsAndEmployees

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import by.devsgroup.employees.ui.viewModel.EmployeeViewModel
import by.devsgroup.groups.ui.viewModel.GroupViewModel
import by.devsgroup.iis.navController.navigateFinal
import by.devsgroup.iis.navigation.ScreenNav
import by.devsgroup.schedule.ui.viewModel.PreviewScheduleViewModel
import by.devsgroup.schedule.ui.viewModel.ScheduleViewModel

@Composable
fun GroupsAndEmployeesScreen(
    groupViewModel: GroupViewModel,
    employeeViewModel: EmployeeViewModel,
    scheduleViewModel: ScheduleViewModel,
    previewScheduleViewModel: PreviewScheduleViewModel,
) {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val tabs = listOf(
        "Группы" to ScreenNav.AllGroups.route,
        "Преподаватели" to ScreenNav.AllEmployees.route,
    )

    var selectedTabIndex by remember { mutableStateOf(0) }

    Column {
        PrimaryTabRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(14.dp, 14.dp, 4.dp, 4.dp)),
            selectedTabIndex = selectedTabIndex,
        ) {
            tabs.forEachIndexed { index, (title, route) ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        if (currentRoute != route) {
                            selectedTabIndex = index
                            navController.navigateFinal(route)
                        }
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
            previewScheduleViewModel = previewScheduleViewModel,
            scheduleViewModel = scheduleViewModel,
            employeeViewModel = employeeViewModel,
            groupViewModel = groupViewModel,
            navController = navController,
        )
    }

}


