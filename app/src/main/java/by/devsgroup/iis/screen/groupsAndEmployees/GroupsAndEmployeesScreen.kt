package by.devsgroup.iis.screen.groupsAndEmployees

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun GroupsAndEmployeesScreen() {
    val navController = rememberNavController()

    Scaffold { paddingValues ->
        PrimaryTabRow(
            modifier = Modifier
                .padding(paddingValues),
            selectedTabIndex = 0,
        ) {
            Tab(
                selected = true,
                onClick = {

                },
                text = {
                    Text(
                        text = "Группы"
                    )
                }
            )
        }
    }

}