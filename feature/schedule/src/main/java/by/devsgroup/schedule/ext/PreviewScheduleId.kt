package by.devsgroup.schedule.ext

import by.devsgroup.domain.model.schedule.preview.PreviewSchedule

fun PreviewSchedule.getScheduleId(): Long? {
    return this.group?.id ?: this.employee?.id
}


