package com.bsuir.schedule.ext

import com.bsuir.domain.model.schedule.common.ScheduleType
import com.bsuir.domain.model.schedule.preview.PreviewSchedule

fun PreviewSchedule.getScheduleType(): ScheduleType? {
    return if (this.group != null) ScheduleType.GROUP else if (this.employee != null) ScheduleType.EMPLOYEE else null
}


