package com.bsuir.bsuirschedule.screen.groupsAndEmployees

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
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bsuir.employees.ui.viewModel.EmployeeViewModel
import com.bsuir.groups.ui.viewModel.GroupViewModel
import com.bsuir.bsuirschedule.navController.navigateFinal
import com.bsuir.bsuirschedule.navigation.ScreenNav
import com.bsuir.schedule.ui.viewModel.PreviewScheduleViewModel
import com.bsuir.schedule.ui.viewModel.ScheduleViewModel

@Composable
fun GroupsAndEmployeesScreen(
    navController: NavController,
    groupViewModel: GroupViewModel,
    employeeViewModel: EmployeeViewModel,
    scheduleViewModel: ScheduleViewModel,
    previewScheduleViewModel: PreviewScheduleViewModel,
) {
    val nestedNavController = rememberNavController()

    val currentBackStackEntry by nestedNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val tabs = listOf(
        "Группы" to ScreenNav.AllGroups.route,
        "Преподаватели" to ScreenNav.AllEmployees.route,
    )

    var selectedTabIndex by remember { mutableStateOf(0) }

    val onRedirectToSchedule = {
        navController.navigateFinal(ScreenNav.Home.route)
    }

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
                            nestedNavController.navigateFinal(route)
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
            onRedirectToSchedule = onRedirectToSchedule,
            scheduleViewModel = scheduleViewModel,
            employeeViewModel = employeeViewModel,
            groupViewModel = groupViewModel,
            navController = nestedNavController,
        )
    }

}


