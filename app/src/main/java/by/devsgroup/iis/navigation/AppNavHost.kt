package by.devsgroup.iis.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import by.devsgroup.employees.ui.viewModel.EmployeeViewModel
import by.devsgroup.groups.ui.viewModel.GroupViewModel
import by.devsgroup.iis.screen.groupsAndEmployees.GroupsAndEmployeesScreen
import by.devsgroup.iis.screen.home.HomeScreen
import by.devsgroup.iis.screen.splash.SplashScreen
import by.devsgroup.iis.ui.component.bottomBar.BottomNavigationBar
import by.devsgroup.iis.ui.component.topBar.TopNavigationBar

@Composable
fun AppNavHost(
    navController: NavHostController,
    drawerState: DrawerState,
    employeeViewModel: EmployeeViewModel,
    groupViewModel: GroupViewModel,
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val topBarIncluded = listOf(
        ScreenNav.Home.route,
    )

    val bottomBarIncluded = listOf(
        ScreenNav.Home.route,
    )

    Scaffold(
        modifier = Modifier
            .animateContentSize(),
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
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = ScreenNav.Splash.route,
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
                    SplashScreen(
                        navController = navController,
                    )
                }
                composable(
                    route = ScreenNav.Home.route
                ) {
                    HomeScreen()
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