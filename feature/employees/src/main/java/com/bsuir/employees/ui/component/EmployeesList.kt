package com.bsuir.employees.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.bsuir.domain.status.loading.LoadingStatus
import com.bsuir.employees.ui.item.EmployeeItem
import com.bsuir.employees.ui.model.EmployeeUI
import com.bsuir.employees.ui.viewModel.EmployeeViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EmployeesList(
    existingEmployeeUrls: List<String>,
    employeeViewModel: EmployeeViewModel,
    onClick: (EmployeeUI) -> Unit,
) {
    val employees = employeeViewModel.employeesPagingFlow.collectAsLazyPagingItems()

    val loadingStatus = employeeViewModel.allEmployeesLoading.collectAsStateWithLifecycle()

    PullToRefreshBox(
        isRefreshing = loadingStatus.value is LoadingStatus.Loading,
        onRefresh = { employeeViewModel.loadAllDepartmentsAndEmployees() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(employees.itemCount, { employees[it]?.uniqueListId ?: it }) { index ->
                val employee = employees[index]

                if (!employee?.urlId.isNullOrEmpty()) {
                    EmployeeItem(
                        employeeUI = employee,
                        downloaded = existingEmployeeUrls.contains(employee.urlId),
                        shapes = ListItemDefaults.segmentedShapes(
                            index = index,
                            count = employees.itemCount
                        ),
                        onClick = {
                            onClick(employee)
                        },
                    )
                }
            }
        }
    }

}