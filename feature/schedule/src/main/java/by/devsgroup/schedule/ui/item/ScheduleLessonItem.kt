package by.devsgroup.schedule.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import by.devsgroup.domain.model.schedule.full.FullScheduleLesson

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ScheduleLessonItem(
    shapes: ListItemShapes,
    scheduleLesson: FullScheduleLesson
) {

    val subject = scheduleLesson.subject ?: "---"
    val subjectFull = scheduleLesson.subjectFullName ?: "---"

    SegmentedListItem(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        onClick = {},
        shapes = shapes,
        leadingContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = scheduleLesson.startLessonTime ?: "--:--",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = scheduleLesson.endLessonTime ?: "--:--",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        supportingContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = subject,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = subjectFull,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    ) {
    }

}