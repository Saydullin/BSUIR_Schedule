package by.devsgroup.groups.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import by.devsgroup.groups.ui.viewModel.GroupViewModel
import by.devsgroup.ui_kit.item.ListItem

@Composable
fun GroupsList(
    groupViewModel: GroupViewModel,
) {

    val groups = groupViewModel.groupsPagingFlow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(groups.itemCount) { index ->
            ListItem(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = groups[index]?.name ?: "---"
                )
            }
        }
    }

}