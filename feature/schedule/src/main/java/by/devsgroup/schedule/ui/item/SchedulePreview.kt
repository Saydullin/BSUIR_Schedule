package by.devsgroup.schedule.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import by.devsgroup.domain.model.schedule.preview.PreviewSchedule

@Composable
fun SchedulePreview(
    schedule: PreviewSchedule
) {

    Column {
        schedule.group?.let { groupSchedule ->
            ScheduleGroupPreview(groupSchedule)
        }

        schedule.employee?.let { employeeSchedule ->
            ScheduleEmployeePreview(employeeSchedule)
        }
    }

}