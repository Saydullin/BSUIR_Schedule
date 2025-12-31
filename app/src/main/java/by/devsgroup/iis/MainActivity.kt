package by.devsgroup.iis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import by.devsgroup.employees.ui.viewModel.EmployeeViewModel
import by.devsgroup.groups.ui.viewModel.GroupViewModel
import by.devsgroup.iis.navigation.AppNavHost
import by.devsgroup.iis.ui.component.drawerSheet.AppModalDrawerSheet
import by.devsgroup.iis.ui.theme.IisTheme
import by.devsgroup.schedule.ui.viewModel.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val groupViewModel: GroupViewModel by viewModels()
    private val employeeViewModel: EmployeeViewModel by viewModels()
    private val scheduleViewModel: ScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

            IisTheme {
                ModalNavigationDrawer(
                    modifier = Modifier.combinedClickable(
                        onClick = {
                            scheduleViewModel.loadSchedule()
                        },
                        onLongClick = {
                            scheduleViewModel.getSchedule()
                        }
                    ),
                    drawerState = drawerState,
                    drawerContent = {
                        AppModalDrawerSheet()
                    }
                ) {
                    AppNavHost(
                        navController = navController,
                        drawerState = drawerState,
                        groupViewModel = groupViewModel,
                        employeeViewModel = employeeViewModel,
                        scheduleViewModel = scheduleViewModel,
                    )
                }
            }
        }
    }
}


