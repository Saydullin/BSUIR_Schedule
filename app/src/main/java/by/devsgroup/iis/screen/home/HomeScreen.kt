package by.devsgroup.iis.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import by.devsgroup.schedule.ui.component.ScheduleList
import by.devsgroup.schedule.ui.viewModel.ScheduleViewModel

@Composable
fun HomeScreen(
    scheduleViewModel: ScheduleViewModel
) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ScheduleList(
            scheduleViewModel = scheduleViewModel
        )
    }

}