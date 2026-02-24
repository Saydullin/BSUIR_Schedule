package by.devsgroup.iis.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.devsgroup.schedule.ui.component.ScheduleList
import by.devsgroup.schedule.ui.viewModel.ScheduleViewModel

@Composable
fun HomeScreen(
    scheduleViewModel: ScheduleViewModel
) {

    val currentScheduleId = scheduleViewModel.currentScheduleId.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (currentScheduleId.value != null) {
            ScheduleList(
                scheduleViewModel = scheduleViewModel
            )
        } else {
            Text(
                modifier = Modifier
                    .padding(32.dp),
                text = "Смахните вправо и выберите расписание",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }

}


