package by.devsgroup.employees.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import by.devsgroup.employees.ui.viewModel.EmployeeViewModel
import by.devsgroup.ui_kit.item.ListItem

@Composable
fun EmployeesList(
    employeeViewModel: EmployeeViewModel,
) {

    val groups = employeeViewModel.employeesPagingFlow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(groups.itemCount, { groups[it]?.uniqueListId ?: it }) { index ->
            ListItem(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = groups[index]?.firstName ?: "---"
                )
            }
        }
    }

}