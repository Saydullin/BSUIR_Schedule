package by.devsgroup.schedule.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import by.devsgroup.schedule.ui.item.ScheduleDayItem
import by.devsgroup.schedule.ui.item.SchedulePreview
import by.devsgroup.schedule.ui.viewModel.ScheduleViewModel

@Composable
fun ScheduleList(
    scheduleViewModel: ScheduleViewModel
) {
    val currentSchedule = scheduleViewModel.currentSchedule.collectAsStateWithLifecycle()
    val scheduleDaysPaging = scheduleViewModel.scheduleDaysFlow.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        scheduleViewModel.getSchedule()
    }

    LaunchedEffect(currentSchedule.value) {
        val schedule = currentSchedule.value
        println("setCurrentScheduleId schedule $schedule")

        val scheduleId = schedule?.group?.id ?: schedule?.employee?.id

        println("setCurrentScheduleId scheduleId $scheduleId")

        if (scheduleId != null) {
            scheduleViewModel.setCurrentScheduleId(scheduleId)
        }
    }

    val schedule = currentSchedule.value
    val scheduleDays = schedule?.schedules

    if (scheduleDays.isNullOrEmpty()) {
        Text(
            text = "Расписание пустое пока"
        )
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                vertical = 16.dp
            )
        ) {
            item {
                SchedulePreview(
                    schedule = schedule
                )
            }

            items(scheduleDaysPaging.itemCount) { index ->
                val scheduleDay = scheduleDaysPaging[index]

                if (scheduleDay != null) {
                    ScheduleDayItem(
                        scheduleDay = scheduleDay
                    )
                }
            }
        }
    }

}