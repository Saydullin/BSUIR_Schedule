package com.bsuir.schedule.ui.component

import android.content.ClipData
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bsuir.domain.model.schedule.common.ScheduleEmployee
import com.bsuir.ui_kit.R
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun EmployeeSchedulePreviewModalInfo(
    schedule: ScheduleEmployee,
    onDeleteSchedule: (scheduleId: Long) -> Unit,
    onUpdateSchedule: (employeeUrlId: String) -> Unit,
) {
    val clipboardManager = LocalClipboard.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(top = 0.dp, start = 32.dp, end = 32.dp, bottom = 32.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(246.dp)
                    .clip(RoundedCornerShape(32.dp)),
                model = schedule.photoLink,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = schedule.fullName(),
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(Modifier.height(16.dp))

        schedule.degree?.let { degree ->
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = degree,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
            )
        }
        schedule.email?.let { email ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            clipboardManager.setClipEntry(
                                ClipEntry(
                                    ClipData.newPlainText("Почта ${schedule.fullName()}", email)
                                )
                            )
                        }
                    },
                text = email,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = {
                    schedule.id?.let { scheduleId ->
                        onDeleteSchedule(scheduleId)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp, 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_trash),
                        contentDescription = "Delete"
                    )

                    Text(
                        text = "Удалить",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

            Button(
                onClick = {
                    schedule.urlId?.let { employeeUrlId ->
                        onUpdateSchedule(employeeUrlId)
                    }
                },
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp, 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_update),
                        contentDescription = "Update"
                    )

                    Text(
                        text = "Обновить",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }

    }

}


