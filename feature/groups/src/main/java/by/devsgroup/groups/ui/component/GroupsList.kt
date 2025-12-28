package by.devsgroup.groups.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import by.devsgroup.groups.ui.item.GroupItem
import by.devsgroup.groups.ui.viewModel.GroupViewModel

@Composable
fun GroupsList(
    groupViewModel: GroupViewModel,
) {
    val groups = groupViewModel.groupsPagingFlow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(groups.itemCount, { groups[it]?.uniqueListId ?: it }) { index ->
            val group = groups[index]

            if (group != null && !group.name.isNullOrBlank()) {
                GroupItem(
                    group = group
                )
            }
        }
    }

}