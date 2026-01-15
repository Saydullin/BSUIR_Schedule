package by.devsgroup.schedule.mapper.context

import by.devsgroup.domain.model.schedule.common.ScheduleEmployee
import by.devsgroup.domain.model.schedule.common.ScheduleGroup
import by.devsgroup.domain.model.schedule.common.ScheduleType

data class ScheduleLessonToFullMapperContext(
    val group: ScheduleGroup?,
    val employee: ScheduleEmployee?,
    val scheduleType: ScheduleType,
)


