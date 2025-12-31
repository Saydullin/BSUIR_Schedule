package by.devsgroup.schedule.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.devsgroup.schedule.ui.item.ScheduleDayItem
import by.devsgroup.schedule.ui.viewModel.ScheduleViewModel

@Composable
fun ScheduleList(
    scheduleViewModel: ScheduleViewModel
) {
    val currentSchedule = scheduleViewModel.currentSchedule.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        scheduleViewModel.getSchedule()
    }

    val scheduleDays = currentSchedule.value?.schedules

    if (scheduleDays.isNullOrEmpty()) {
        Text(
            text = "Расписание пустое пока"
        )
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(scheduleDays) {
                ScheduleDayItem(it)
            }
        }
    }

}