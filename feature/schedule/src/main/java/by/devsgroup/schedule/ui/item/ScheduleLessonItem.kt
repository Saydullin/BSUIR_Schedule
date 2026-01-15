package by.devsgroup.schedule.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import by.devsgroup.domain.model.schedule.full.FullScheduleLesson
import by.devsgroup.schedule.ext.lessonTypeAbbrevParse
import by.devsgroup.schedule.ext.lessonTypeColorParse

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ScheduleLessonItem(
    shapes: ListItemShapes,
    scheduleLesson: FullScheduleLesson
) {

    val subject = scheduleLesson.subject ?: "---"
    val subjectFull = scheduleLesson.subjectFullName ?: "---"
    val subjectEmployees = scheduleLesson.employees?.joinToString(", ") { it.middleName ?: "" } ?: ""
    val subjectGroups = scheduleLesson.studentGroups?.joinToString(", ") { it.name ?: "" } ?: ""
    val subjectType = scheduleLesson.lessonTypeAbbrevParse()
    val subjectTypeColor = scheduleLesson.lessonTypeColorParse()

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
        overlineContent = {
            Box(
                modifier = Modifier
                    .padding(bottom = 7.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(subjectTypeColor),
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 3.dp, horizontal = 7.dp),
                    text = subjectType.lowercase(),
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
                if (subjectEmployees.isNotEmpty()) {
                    Text(
                        text = subjectEmployees,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                if (subjectGroups.isNotEmpty()) {
                    Text(
                        text = subjectGroups,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    ) {
    }

}