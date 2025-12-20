package by.devsgroup.iis.navController

import androidx.navigation.NavController

fun NavController.navigateInclusive(
    route: String,
    popUpToInclusive: Boolean = true,
    singleTop: Boolean = true
) {
    currentBackStackEntry?.destination?.route?.let { current ->
        navigate(route) {
            popUpTo(current) {
                inclusive = popUpToInclusive
            }
            launchSingleTop = singleTop
        }
    }
}