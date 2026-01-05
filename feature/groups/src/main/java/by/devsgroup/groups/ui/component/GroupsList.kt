package by.devsgroup.groups.ui.component

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
import by.devsgroup.groups.ui.item.GroupItem
import by.devsgroup.groups.ui.model.GroupUI
import by.devsgroup.groups.ui.viewModel.GroupViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GroupsList(
    groupViewModel: GroupViewModel,
    onClick: (GroupUI) -> Unit,
) {
    val groups = groupViewModel.groupsPagingFlow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(groups.itemCount, { groups[it]?.uniqueListId ?: it }) { index ->
            val group = groups[index]

            if (!group?.name.isNullOrBlank()) {
                GroupItem(
                    group = group,
                    shapes = ListItemDefaults.segmentedShapes(
                        index = index,
                        count = groups.itemCount
                    ),
                    onClick = {
                        onClick(group)
                    },
                )
            }
        }
    }

}