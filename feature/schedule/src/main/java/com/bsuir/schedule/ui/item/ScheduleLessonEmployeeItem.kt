package com.bsuir.schedule.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.bsuir.domain.model.schedule.full.FullScheduleLesson
import com.bsuir.schedule.ext.lessonTypeAbbrevParse
import com.bsuir.schedule.ext.lessonTypeColorParse

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ScheduleLessonEmployeeItem(
    shapes: ListItemShapes,
    scheduleLesson: FullScheduleLesson
) {

    val subject = scheduleLesson.subject ?: "---"
    val subjectType = scheduleLesson.lessonTypeAbbrevParse()
    val subjectTypeColor = scheduleLesson.lessonTypeColorParse()
    val subjectEmployees = scheduleLesson.employees
        ?.filterNot { it.urlId == scheduleLesson.scheduleEmployee?.urlId }
        ?.joinToString(", ") { it.fullName() } ?: ""
    val subjectGroups = scheduleLesson.studentGroups
        ?.filterNot { it.name == scheduleLesson.scheduleGroup?.name }
        ?.joinToString(", ") { it.name.orEmpty() }
        .orEmpty()

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
                modifier = Modifier
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = subject,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(subjectTypeColor),
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 3.dp, horizontal = 7.dp),
                            text = subjectType.lowercase(),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
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