package by.devsgroup.schedule.ui.component

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import by.devsgroup.domain.model.schedule.common.ScheduleEmployee
import by.devsgroup.domain.model.schedule.common.ScheduleGroup
import coil.compose.AsyncImage

@Composable
fun EmployeeSchedulePreviewModalInfo(
    schedule: ScheduleEmployee
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
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
                    .size(200.dp)
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
                text = degree,
                style = MaterialTheme.typography.titleSmall,
            )
        }
        schedule.email?.let { email ->
            Text(
                text = email,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }

}


