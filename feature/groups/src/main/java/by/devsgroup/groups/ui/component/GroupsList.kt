package by.devsgroup.groups.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
        items(groups.itemCount, { groups[it]?.uniqueListId ?: it }) { index ->
            val group = groups[index]
            val course = if (group?.course != null) "${group.course} курс" else ""

            if (group != null && !group.name.isNullOrBlank()) {
                ListItem(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = group.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = course,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "${group.facultyAbbrev} ${group.specialityAbbrev}".trim(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }

}