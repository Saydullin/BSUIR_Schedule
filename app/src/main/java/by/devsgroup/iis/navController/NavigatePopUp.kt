package by.devsgroup.iis.navController

import androidx.navigation.NavController
import by.devsgroup.iis.navigation.ScreenNav

fun NavController.navigatePopUp(
    route: String,
    popUpTo: String? = this.graph.startDestinationRoute,
    popUpToInclusive: Boolean = true,
    singleTop: Boolean = true
) {
    navigate(route) {
        popUpTo(popUpTo ?: ScreenNav.Home.route) {
            inclusive = popUpToInclusive
        }
        launchSingleTop = singleTop
    }
}


