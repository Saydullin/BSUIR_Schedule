package com.bsuir.bsuirschedule.screen.groupsAndEmployees

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bsuir.employees.ui.viewModel.EmployeeViewModel
import com.bsuir.groups.ui.viewModel.GroupViewModel
import com.bsuir.bsuirschedule.navigation.ScreenNav
import com.bsuir.bsuirschedule.screen.employees.EmployeesScreen
import com.bsuir.bsuirschedule.screen.groups.GroupsScreen
import com.bsuir.schedule.ui.viewModel.PreviewScheduleViewModel
import com.bsuir.schedule.ui.viewModel.ScheduleViewModel

@Composable
fun GroupsAndEmployeesNavHost(
    previewScheduleViewModel: PreviewScheduleViewModel,
    scheduleViewModel: ScheduleViewModel,
    employeeViewModel: EmployeeViewModel,
    groupViewModel: GroupViewModel,
    navController: NavHostController,
    onRedirectToSchedule: () -> Unit,
) {

    NavHost(
        navController = navController,
        startDestination = ScreenNav.AllGroups.route,
        enterTransition = {
            slideInVertically(
                initialOffsetY = { -40 },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutVertically(
                targetOffsetY = { -40 },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInVertically(
                initialOffsetY = { -40 },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutVertically(
                targetOffsetY = { -40 },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        composable(
            route = ScreenNav.AllGroups.route
        ) {
            GroupsScreen(
                navController = navController,
                groupViewModel = groupViewModel,
                scheduleViewModel = scheduleViewModel,
                previewScheduleViewModel = previewScheduleViewModel,
                onRedirectToSchedule = onRedirectToSchedule,
            )
        }
        composable(
            route = ScreenNav.AllEmployees.route
        ) {
            EmployeesScreen(
                navController = navController,
                employeeViewModel = employeeViewModel,
                scheduleViewModel = scheduleViewModel,
                previewScheduleViewModel = previewScheduleViewModel,
                onRedirectToSchedule = onRedirectToSchedule,
            )
        }
    }

}


