package by.devsgroup.iis.screen.groups

import androidx.compose.runtime.Composable
import by.devsgroup.groups.ui.component.GroupsList
import by.devsgroup.groups.ui.viewModel.GroupViewModel

@Composable
fun GroupsScreen(
    groupViewModel: GroupViewModel
) {

    GroupsList(
        groupViewModel = groupViewModel,
    )

}