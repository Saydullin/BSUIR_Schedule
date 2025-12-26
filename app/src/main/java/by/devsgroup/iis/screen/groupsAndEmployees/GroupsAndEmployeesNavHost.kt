package by.devsgroup.iis.screen.groupsAndEmployees

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import by.devsgroup.employees.ui.viewModel.EmployeeViewModel
import by.devsgroup.groups.ui.viewModel.GroupViewModel
import by.devsgroup.iis.navigation.ScreenNav
import by.devsgroup.iis.screen.employees.EmployeesScreen
import by.devsgroup.iis.screen.groups.GroupsScreen

@Composable
fun GroupsAndEmployeesNavHost(
    employeeViewModel: EmployeeViewModel,
    groupViewModel: GroupViewModel,
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = ScreenNav.AllGroups.route,
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
            route = ScreenNav.AllGroups.route
        ) {
            GroupsScreen(
                groupViewModel = groupViewModel,
            )
        }
        composable(
            route = ScreenNav.AllEmployees.route
        ) {
            EmployeesScreen(
                employeeViewModel = employeeViewModel,
            )
        }
    }

}