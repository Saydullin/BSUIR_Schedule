package com.bsuir.schedule.ext

import com.bsuir.domain.model.schedule.preview.PreviewSchedule

fun PreviewSchedule.getScheduleId(): Long? {
    return this.group?.id ?: this.employee?.id
}


