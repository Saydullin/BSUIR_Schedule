package by.devsgroup.iis.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import by.devsgroup.employees.ui.viewModel.EmployeeViewModel
import by.devsgroup.groups.ui.viewModel.GroupViewModel
import by.devsgroup.iis.screen.groupsAndEmployees.GroupsAndEmployeesScreen
import by.devsgroup.iis.screen.home.HomeScreen
import by.devsgroup.iis.ui.component.bottomBar.BottomNavigationBar
import by.devsgroup.iis.ui.component.topBar.TopNavigationBar
import by.devsgroup.schedule.ui.viewModel.ScheduleViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    drawerState: DrawerState,
    employeeViewModel: EmployeeViewModel,
    scheduleViewModel: ScheduleViewModel,
    groupViewModel: GroupViewModel,
) {

    Scaffold(
        topBar = {
            TopNavigationBar(
                navController = navController,
                drawerState = drawerState
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = ScreenNav.Home.route,
                enterTransition = {
                    fadeIn(animationSpec = tween(200))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(200))
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(200))
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(200))
                }
            ) {
                composable(
                    route = ScreenNav.Home.route
                ) {
                    HomeScreen(
                        scheduleViewModel = scheduleViewModel,
                    )
                }
                composable(
                    route = ScreenNav.Schedule.route
                ) {
                    GroupsAndEmployeesScreen(
                        employeeViewModel = employeeViewModel,
                        groupViewModel = groupViewModel,
                    )
                }
                composable(
                    route = ScreenNav.Exams.route
                ) {
                    GroupsAndEmployeesScreen(
                        employeeViewModel = employeeViewModel,
                        groupViewModel = groupViewModel,
                    )
                }
                composable(
                    route = ScreenNav.AllGroupsAndEmployees.route
                ) {
                    GroupsAndEmployeesScreen(
                        employeeViewModel = employeeViewModel,
                        groupViewModel = groupViewModel,
                    )
                }
            }
        }
    }

}