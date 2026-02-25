package com.bsuir.bsuirschedule.screen.groups

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bsuir.groups.ui.component.GroupsList
import com.bsuir.groups.ui.model.GroupUI
import com.bsuir.groups.ui.viewModel.GroupViewModel
import com.bsuir.schedule.ui.model.PreviewScheduleType
import com.bsuir.schedule.ui.viewModel.PreviewScheduleViewModel
import com.bsuir.schedule.ui.viewModel.ScheduleViewModel
import com.bsuir.ui_kit.dialog.DialogModal
import com.bsuir.ui_kit.search.TextSearch

@Composable
fun GroupsScreen(
    navController: NavController,
    groupViewModel: GroupViewModel,
    scheduleViewModel: ScheduleViewModel,
    previewScheduleViewModel: PreviewScheduleViewModel,
    onRedirectToSchedule: () -> Unit,
) {
    val context = LocalContext.current
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
        val groupId = group.id
        val groupName = group.name
        val loaded = existingGroupNames.contains(groupName)

        if (groupName == null || groupId == null) {
            DialogModal(
                title = group.name ?: "Неизвестная группа",
                description = "Сервер вернул какую-то странную группу. Ее не получится загрузить",
                positiveButtonText = "Понятно",
                negativeButtonText = "Какой ужас",
                onSkip = { selectedGroup = null },
                onDismiss = { selectedGroup = null },
                onConfirm = { selectedGroup = null }
            )

            return@let
        }

        if (loaded) {
            DialogModal(
                title = group.name ?: "Неизвестная группа",
                description = "Расписание уже загружено",
                positiveButtonText = "Открыть",
                negativeButtonText = "Отмена",
                onSkip = { selectedGroup = null },
                onDismiss = { selectedGroup = null },
                onConfirm = {
                    selectedGroup = null

                    scheduleViewModel.loadGroupSchedule(groupName)

                    scheduleViewModel.setCurrentScheduleByGroupId(groupId)

                    onRedirectToSchedule()
                }
            )
        } else {
            DialogModal(
                title = group.name ?: "Неизвестная группа",
                description = "Загрузить расписание этой группы?",
                positiveButtonText = "Загрузить",
                negativeButtonText = "Отмена",
                onSkip = { selectedGroup = null },
                onDismiss = { selectedGroup = null },
                onConfirm = {
                    Toast.makeText(context, "Загрузка расписания ...", Toast.LENGTH_SHORT).show()

                    group.name?.let { groupName ->
                        scheduleViewModel.loadGroupSchedule(groupName)
                        selectedGroup = null
                    }
                }
            )
        }
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


