package by.devsgroup.iis.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import by.devsgroup.employees.ui.viewModel.EmployeeViewModel
import by.devsgroup.groups.ui.viewModel.GroupViewModel
import by.devsgroup.iis.screen.groupsAndEmployees.GroupsAndEmployeesScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    employeeViewModel: EmployeeViewModel,
    groupViewModel: GroupViewModel,
) {

    NavHost(
        navController = navController,
        startDestination = ScreenNav.AllGroupsAndEmployees.route,
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
            route = ScreenNav.Splash.route
        ) {
            Text(
                text = "Splash Screen"
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