package by.devsgroup.schedule.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import by.devsgroup.domain.model.schedule.full.FullScheduleDay
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ScheduleDayItem(
    scheduleDay: FullScheduleDay
) {
    val formattedDate = try {
        val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale.getDefault())

        val date = Instant.ofEpochMilli(scheduleDay.date)
            .atZone(ZoneId.of("Europe/Minsk"))
            .toLocalDate()

        date.format(formatter)
    } catch (e: Exception) {
        e.printStackTrace()

        "Неизвестная дата"
    }

    val lessons = scheduleDay.lessons

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = formattedDate,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(Modifier.height(4.dp))

        if (!lessons.isNullOrEmpty()) {
            lessons.forEachIndexed { index, lesson ->
                ScheduleLessonItem(
                    shapes = ListItemDefaults.segmentedShapes(
                        index = index,
                        count = lessons.size
                    ),
                    scheduleLesson = lesson
                )
            }
        }
    }

}