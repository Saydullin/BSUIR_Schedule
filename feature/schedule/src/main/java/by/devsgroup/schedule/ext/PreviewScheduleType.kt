package by.devsgroup.schedule.ext

import by.devsgroup.domain.model.schedule.common.ScheduleType
import by.devsgroup.domain.model.schedule.preview.PreviewSchedule

fun PreviewSchedule.getScheduleType(): ScheduleType? {
    return if (this.group != null) ScheduleType.GROUP else if (this.employee != null) ScheduleType.EMPLOYEE else null
}


