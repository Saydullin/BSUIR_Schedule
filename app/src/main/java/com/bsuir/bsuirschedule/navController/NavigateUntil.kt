package com.bsuir.bsuirschedule.navController

import androidx.navigation.NavController

fun NavController.navigateUntil(
    route: String,
    popUpToInclusive: Boolean = true,
    singleTop: Boolean = true
) {
    navigate(route) {
        popUpTo(route) {
            inclusive = popUpToInclusive
        }
        launchSingleTop = singleTop
    }
}


