package by.devsgroup.schedule.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import by.devsgroup.domain.model.schedule.common.ScheduleEmployee
import by.devsgroup.domain.model.schedule.full.FullSchedule
import by.devsgroup.domain.model.schedule.preview.PreviewSchedule
import coil.compose.AsyncImage

@Composable
fun ScheduleEmployeePreview(
    schedule: ScheduleEmployee
) {
    val scheduleName = schedule.fullName()

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        onClick = {  }
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


