package by.devsgroup.schedule.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

    if (scheduleDaysPaging.itemCount == 0) {
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
            schedulePreview?.let { schedule ->
                when(schedule) {
                    LoadingStatus.Loading -> {
                        item {
                            Text(
                                text = "Загрузка"
                            )
                        }
                    }
                    is LoadingStatus.Success -> {
                        item {
                            SchedulePreview(schedule.data)
                        }
                    }
                    is LoadingStatus.Error -> {
                       item {
                           Text(
                               text = "Ошибка ${schedule.type}"
                           )
                       }
                    }
                }
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