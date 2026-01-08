package by.devsgroup.employees.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import by.devsgroup.employees.ui.item.EmployeeItem
import by.devsgroup.employees.ui.model.EmployeeUI
import by.devsgroup.employees.ui.viewModel.EmployeeViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EmployeesList(
    existingEmployeeUrls: List<String>,
    employeeViewModel: EmployeeViewModel,
    onClick: (EmployeeUI) -> Unit,
) {
    val employees = employeeViewModel.employeesPagingFlow.collectAsLazyPagingItems()

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