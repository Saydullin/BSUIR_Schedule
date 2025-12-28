package by.devsgroup.iis.ui.component.bottomNavigationBar

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import by.devsgroup.iis.R
import by.devsgroup.iis.navigation.ScreenNav
import by.devsgroup.iis.ui.model.navigationBar.NavigationBarModel

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    val items = listOf(
        NavigationBarModel(
            route = ScreenNav.Schedule.route,
            icon = painterResource(R.drawable.ic_schedule),
            selectedIcon = painterResource(R.drawable.ic_schedule_filled),
            title = "Расписание"
        ),
        NavigationBarModel(
            route = ScreenNav.Exams.route,
            icon = painterResource(R.drawable.ic_exam),
            selectedIcon = painterResource(R.drawable.ic_exam_filled),
            title = "Экзамены"
        ),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = if (selected) item.selectedIcon else item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                selected = selected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // чтобы не плодить копии в backstack
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
