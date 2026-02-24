package by.devsgroup.schedule.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import by.devsgroup.domain.model.schedule.preview.PreviewSchedule
import by.devsgroup.schedule.ui.viewModel.ScheduleViewModel

@Composable
fun SchedulePreview(
    schedule: PreviewSchedule,
    scheduleViewModel: ScheduleViewModel,
) {

    Column {
        schedule.group?.let { groupSchedule ->
            ScheduleGroupPreview(groupSchedule)
        }

        schedule.employee?.let { employeeSchedule ->
            ScheduleEmployeePreview(
                schedule = employeeSchedule,
                onDeleteSchedule = { scheduleId ->
                    scheduleViewModel.deleteScheduleByEmployeeId(scheduleId)
                },
                onUpdateSchedule = { employeeUrlId ->
                    scheduleViewModel.loadEmployeeSchedule(employeeUrlId)
                }
            )
        }
    }

}