package com.bsuir.bsuirschedule.navController

import androidx.navigation.NavController

fun NavController.navigateFinal(
    route: String,
    popUpToInclusive: Boolean = true,
    singleTop: Boolean = true
) {
    navigate(route) {
        popUpTo(0) {
            inclusive = popUpToInclusive
        }
        launchSingleTop = singleTop
    }
}