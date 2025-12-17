package by.devsgroup.groups.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import by.devsgroup.groups.ui.viewModel.GroupViewModel
import by.devsgroup.ui_kit.item.ListItem

@Composable
fun GroupsList(
    groupViewModel: GroupViewModel,
) {

    val groups = groupViewModel.groupsPagingFlow.collectAsLazyPagingItems()

    LaunchedEffect(groups.loadState.refresh) {
        if (groups.loadState.refresh is LoadState.NotLoading) {
            println("groups.itemCount Loaded items = ${groups.itemCount}")
        }
    }

    println("groups.itemCount ${groups.loadState.refresh}")

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            when (groups.loadState.refresh) {
                is LoadState.Loading -> {
                    Text("Loading...")
                }
                is LoadState.NotLoading -> {
                    Text("Count: ${groups.itemCount}")
                }
                is LoadState.Error -> {
                    Text("Error")
                }
            }
        }

        items(groups.itemCount) { index ->
            ListItem {
                Text(
                    text = groups[index]?.name ?: "---"
                )
            }
        }
    }

}