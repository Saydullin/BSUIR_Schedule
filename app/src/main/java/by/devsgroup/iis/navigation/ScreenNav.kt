package by.devsgroup.iis.navigation

sealed class ScreenNav(
    val route: String
) {

    object Splash: ScreenNav(
        route = "Splash"
    )

    object AllGroupsAndEmployees: ScreenNav(
        route = "AllGroupsAndEmployees"
    )

    object AllGroups: ScreenNav(
        route = "AllGroups"
    )

    object AllEmployees: ScreenNav(
        route = "AllEmployees"
    )

    object Settings: ScreenNav(
        route = "Settings"
    )

    object Schedule: ScreenNav(
        route = "Schedule"
    )

    object Exams: ScreenNav(
        route = "Exams"
    )

    object Home: ScreenNav(
        route = "Home"
    )

    object Status: ScreenNav(
        route = "Status"
    )

}
