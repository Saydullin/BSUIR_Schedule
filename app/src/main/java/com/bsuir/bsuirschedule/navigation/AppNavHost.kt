package com.bsuir.bsuirschedule.navigation

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
import com.bsuir.employees.ui.viewModel.EmployeeViewModel
import com.bsuir.groups.ui.viewModel.GroupViewModel
import com.bsuir.bsuirschedule.screen.groupsAndEmployees.GroupsAndEmployeesScreen
import com.bsuir.bsuirschedule.screen.home.HomeScreen
import com.bsuir.bsuirschedule.ui.component.topBar.TopNavigationBar
import com.bsuir.schedule.ui.viewModel.PreviewScheduleViewModel
import com.bsuir.schedule.ui.viewModel.ScheduleViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    drawerState: DrawerState,
    employeeViewModel: EmployeeViewModel,
    scheduleViewModel: ScheduleViewModel,
    groupViewModel: GroupViewModel,
    previewScheduleViewModel: PreviewScheduleViewModel
) {

    Scaffold(
        topBar = {
            TopNavigationBar(
                navController = navController,
                drawerState = drawerState
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = ScreenNav.Home.route,
                enterTransition = {
                    fadeIn(animationSpec = tween(300))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(300))
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(300))
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(300))
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
                        navController = navController,
                        scheduleViewModel = scheduleViewModel,
                        employeeViewModel = employeeViewModel,
                        groupViewModel = groupViewModel,
                        previewScheduleViewModel = previewScheduleViewModel,
                    )
                }
                composable(
                    route = ScreenNav.Exams.route
                ) {
                    GroupsAndEmployeesScreen(
                        navController = navController,
                        scheduleViewModel = scheduleViewModel,
                        employeeViewModel = employeeViewModel,
                        groupViewModel = groupViewModel,
                        previewScheduleViewModel = previewScheduleViewModel,
                    )
                }
                composable(
                    route = ScreenNav.AllGroupsAndEmployees.route
                ) {
                    GroupsAndEmployeesScreen(
                        navController = navController,
                        scheduleViewModel = scheduleViewModel,
                        employeeViewModel = employeeViewModel,
                        groupViewModel = groupViewModel,
                        previewScheduleViewModel = previewScheduleViewModel,
                    )
                }
            }
        }
    }

}