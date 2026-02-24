package by.devsgroup.schedule.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import by.devsgroup.domain.status.loading.LoadingStatus
import by.devsgroup.schedule.ui.item.ScheduleDayItem
import by.devsgroup.schedule.ui.item.SchedulePreview
import by.devsgroup.schedule.ui.viewModel.ScheduleViewModel

@Composable
fun ScheduleList(
    scheduleViewModel: ScheduleViewModel
) {
    val currentSchedulePreview = scheduleViewModel.currentSchedulePreview.collectAsStateWithLifecycle()
    val scheduleDaysPaging = scheduleViewModel.scheduleDaysFlow.collectAsLazyPagingItems()

    val schedulePreview = currentSchedulePreview.value

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        schedulePreview?.let { schedule ->
            when (schedule) {
                LoadingStatus.Loading -> {
                    Text(
                        text = "Загрузка"
                    )
                }

                is LoadingStatus.Success -> {
                    SchedulePreview(
                        schedule = schedule.data,
                        scheduleViewModel = scheduleViewModel,
                    )
                }

                is LoadingStatus.Error -> {
                    Text(
                        text = "Ошибка ${schedule.type}"
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                vertical = 16.dp
            )
        ) {
            items(scheduleDaysPaging.itemCount) { index ->
                val scheduleDay = scheduleDaysPaging[index]

                if (scheduleDay != null) {
                    ScheduleDayItem(
                        modifier = Modifier
                            .animateItem(),
                        scheduleDay = scheduleDay
                    )
                }
            }
        }
    }

}


