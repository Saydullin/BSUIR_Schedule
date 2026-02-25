package com.bsuir.schedule.mapper.context

import com.bsuir.domain.model.schedule.common.ScheduleEmployee
import com.bsuir.domain.model.schedule.common.ScheduleGroup
import com.bsuir.domain.model.schedule.common.ScheduleType

data class ScheduleLessonToFullMapperContext(
    val group: ScheduleGroup?,
    val employee: ScheduleEmployee?,
    val scheduleType: ScheduleType,
)


