package by.devsgroup.iis.screen.groups

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import by.devsgroup.groups.ui.component.GroupsList
import by.devsgroup.groups.ui.model.GroupUI
import by.devsgroup.groups.ui.viewModel.GroupViewModel
import by.devsgroup.schedule.ui.model.PreviewScheduleType
import by.devsgroup.schedule.ui.viewModel.PreviewScheduleViewModel
import by.devsgroup.schedule.ui.viewModel.ScheduleViewModel
import by.devsgroup.ui_kit.dialog.DialogModal
import by.devsgroup.ui_kit.search.TextSearch

@Composable
fun GroupsScreen(
    groupViewModel: GroupViewModel,
    scheduleViewModel: ScheduleViewModel,
    previewScheduleViewModel: PreviewScheduleViewModel,
) {
    val focusManager = LocalFocusManager.current

    var search by remember { mutableStateOf("") }

    var selectedGroup by remember { mutableStateOf<GroupUI?>(null) }

    val previewScheduleList = previewScheduleViewModel.previewSchedules.collectAsStateWithLifecycle()

    val existingGroupNames: List<String> = previewScheduleList.value
            ?.filterIsInstance<PreviewScheduleType.Group>()
            ?.map { it.name } ?: listOf()

    LaunchedEffect(Unit) {
        scheduleViewModel.scheduleLoaded.collect {
            groupViewModel.updateGroupsList()
        }
    }

    selectedGroup?.let { group ->
        DialogModal(
            title = "Загрузить расписание?",
            description = "Расписание группы ${group.name} можно будет смотреть оффлайн",
            positiveButtonText = "Загрузить",
            negativeButtonText = "Отмена",
            onSkip = { selectedGroup = null },
            onDismiss = { selectedGroup = null },
            onConfirm = {
                group.name?.let { groupName ->
                    scheduleViewModel.loadGroupSchedule(groupName)
                    selectedGroup = null
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            },
    ) {
        TextSearch(
            modifier = Modifier
                .padding(16.dp),
            search = search,
            onSearchChange = {
                search = it

                groupViewModel.setSearch(it)
            }
        )

        GroupsList(
            groupViewModel = groupViewModel,
            existingGroupNames = existingGroupNames,
            onClick = { group ->
                group.name?.let { groupName ->
                    selectedGroup = group
                }
            }
        )
    }

}


