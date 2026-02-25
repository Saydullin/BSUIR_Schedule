package com.bsuir.schedule.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.bsuir.domain.model.schedule.preview.PreviewSchedule
import com.bsuir.schedule.ui.viewModel.ScheduleViewModel

@Composable
fun SchedulePreview(
    schedule: PreviewSchedule,
    scheduleViewModel: ScheduleViewModel,
) {

    Column {
        schedule.group?.let { groupSchedule ->
            ScheduleGroupPreview(
                schedule = groupSchedule,
                onDeleteSchedule = { scheduleId ->
                    scheduleViewModel.deleteScheduleByGroupId(scheduleId)
                },
                onUpdateSchedule = { groupName ->
                    scheduleViewModel.loadGroupSchedule(groupName)
                }
            )
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