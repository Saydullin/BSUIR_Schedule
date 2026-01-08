package by.devsgroup.iis.screen.groupsAndEmployees

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import by.devsgroup.employees.ui.viewModel.EmployeeViewModel
import by.devsgroup.groups.ui.viewModel.GroupViewModel
import by.devsgroup.iis.navigation.ScreenNav
import by.devsgroup.iis.screen.employees.EmployeesScreen
import by.devsgroup.iis.screen.groups.GroupsScreen
import by.devsgroup.schedule.ui.viewModel.PreviewScheduleViewModel
import by.devsgroup.schedule.ui.viewModel.ScheduleViewModel

@Composable
fun GroupsAndEmployeesNavHost(
    previewScheduleViewModel: PreviewScheduleViewModel,
    scheduleViewModel: ScheduleViewModel,
    employeeViewModel: EmployeeViewModel,
    groupViewModel: GroupViewModel,
    navController: NavHostController,
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
            )
        }
    }

}


