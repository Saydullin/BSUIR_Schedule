package by.devsgroup.schedule.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import by.devsgroup.domain.model.schedule.common.ScheduleGroup

@Composable
fun GroupSchedulePreviewModalInfo(
    schedule: ScheduleGroup
) {
    val scrollState = rememberScrollState()

    val courseText = if (schedule.course != null) "${schedule.course} курс" else null

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = schedule.name ?: "Неизвестное название",
                style = MaterialTheme.typography.headlineLarge,
            )

            courseText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = schedule.facultyName ?: "Неизвестный факультет",
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            text = schedule.specialityName ?: "Неизвестная специальность",
            style = MaterialTheme.typography.titleSmall,
        )

        Spacer(Modifier.height(32.dp))
    }

}


