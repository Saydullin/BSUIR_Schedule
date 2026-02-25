package com.bsuir.groups.ui.component

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
import com.bsuir.groups.ui.item.GroupItem
import com.bsuir.groups.ui.model.GroupUI
import com.bsuir.groups.ui.viewModel.GroupViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GroupsList(
    existingGroupNames: List<String>,
    groupViewModel: GroupViewModel,
    onClick: (GroupUI) -> Unit,
) {
    val groups = groupViewModel.groupsPagingFlow.collectAsLazyPagingItems()

    val loadingStatus = groupViewModel.allGroupsLoading.collectAsStateWithLifecycle()

    PullToRefreshBox(
        isRefreshing = loadingStatus.value is LoadingStatus.Loading,
        onRefresh = { groupViewModel.loadAllGroups() }
    ) {
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
                        downloaded = existingGroupNames.contains(group.name),
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

}