package by.devsgroup.groups.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.devsgroup.groups.ui.viewModel.GroupViewModel

@Composable
fun GroupsList(
    groupViewModel: GroupViewModel
) {

    val groupsState = groupViewModel.groups.collectAsStateWithLifecycle()
    val groups = groupsState.value

    if (groups == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("groups == null")
        }

        return
    }

    LazyColumn {
        items(groups) {
            Text(it.name ?: "???")
        }
    }

}