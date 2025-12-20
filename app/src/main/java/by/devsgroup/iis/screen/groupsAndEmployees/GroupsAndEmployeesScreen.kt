package by.devsgroup.iis.screen.groupsAndEmployees

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import by.devsgroup.groups.ui.viewModel.GroupViewModel
import by.devsgroup.iis.navController.navigateFinal
import by.devsgroup.iis.navigation.ScreenNav

@Composable
fun GroupsAndEmployeesScreen(
    groupViewModel: GroupViewModel,
) {
    val navController = rememberNavController()

    val tabs = listOf(
        "Группы" to ScreenNav.AllGroups.route,
        "Преподаватели" to ScreenNav.AllEmployees.route,
    )

    var selectedTabIndex by remember { mutableStateOf(0) }

    Column {
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
        ) {
            tabs.forEachIndexed { index, (title, route) ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        navController.navigateFinal(route)
                    },
                    text = {
                        Text(
                            text = title
                        )
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        GroupsAndEmployeesNavHost(
            groupViewModel = groupViewModel,
            navController = navController,
        )
    }

}


