package com.bsuir.bsuirschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.bsuir.employees.ui.viewModel.EmployeeViewModel
import com.bsuir.groups.ui.viewModel.GroupViewModel
import com.bsuir.bsuirschedule.navigation.AppNavHost
import com.bsuir.bsuirschedule.ui.component.drawerSheet.AppModalDrawerSheet
import com.bsuir.bsuirschedule.ui.theme.IisTheme
import com.bsuir.schedule.ui.viewModel.PreviewScheduleViewModel
import com.bsuir.schedule.ui.viewModel.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val groupViewModel: GroupViewModel by viewModels()
    private val employeeViewModel: EmployeeViewModel by viewModels()
    private val scheduleViewModel: ScheduleViewModel by viewModels()
    private val previewScheduleViewModel: PreviewScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            LaunchedEffect(Unit) {
                groupViewModel.loadAllGroups()
                employeeViewModel.loadAllDepartmentsAndEmployees()
            }

            LaunchedEffect(Unit) {
                scheduleViewModel.scheduleLoaded.collect {
                    if (it != null) {
                        previewScheduleViewModel.getAllSchedules()
                    }
                }
            }

            IisTheme {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        AppModalDrawerSheet(
                            navController = navController,
                            onClose = {
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            scheduleViewModel = scheduleViewModel,
                            previewScheduleViewModel = previewScheduleViewModel,
                        )
                    }
                ) {
                    AppNavHost(
                        navController = navController,
                        drawerState = drawerState,
                        groupViewModel = groupViewModel,
                        employeeViewModel = employeeViewModel,
                        scheduleViewModel = scheduleViewModel,
                        previewScheduleViewModel = previewScheduleViewModel,
                    )
                }
            }
        }
    }
}


