package com.bsuir.schedule.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bsuir.domain.model.schedule.common.ScheduleEmployee
import com.bsuir.schedule.ui.component.EmployeeSchedulePreviewModalInfo
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleEmployeePreview(
    schedule: ScheduleEmployee,
    onDeleteSchedule: (employeeId: Long) -> Unit,
    onUpdateSchedule: (employeeUrlId: String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val scheduleName = schedule.fullName()
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
            EmployeeSchedulePreviewModalInfo(
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
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(32.dp)),
                model = schedule.photoLink,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Text(
                text = scheduleName,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }

}


