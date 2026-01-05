package by.devsgroup.schedule.ext

import by.devsgroup.domain.model.schedule.preview.PreviewSchedule

fun PreviewSchedule.getScheduleName(): String? {
    return this.group?.name ?: this.employee?.fullName()
}