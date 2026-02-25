package com.bsuir.schedule.ext

import com.bsuir.domain.model.schedule.preview.PreviewSchedule

fun PreviewSchedule.getScheduleName(): String? {
    return this.group?.name ?: this.employee?.fullName()
}