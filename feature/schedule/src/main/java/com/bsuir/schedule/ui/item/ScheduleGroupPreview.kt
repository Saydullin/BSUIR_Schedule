package com.bsuir.schedule.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bsuir.domain.model.schedule.common.ScheduleGroup
import com.bsuir.schedule.ui.component.GroupSchedulePreviewModalInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleGroupPreview(
    schedule: ScheduleGroup,
    onDeleteSchedule: (groupId: Long) -> Unit,
    onUpdateSchedule: (groupName: String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val scheduleName = schedule.name.orEmpty()
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            },
            sheetState = sheetState,
        ) {
            GroupSchedulePreviewModalInfo(
                schedule = schedule,
                onDeleteSchedule = {
                    onDeleteSchedule(it)

                    showBottomSheet = false
                },
                onUpdateSchedule = {
                    onUpdateSchedule(it)

                    showBottomSheet = false
                },
            )
        }
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        onClick = {
            showBottomSheet = true

            scope.launch {
                sheetState.show()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = scheduleName,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }

}