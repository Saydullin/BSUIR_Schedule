package com.bsuir.schedule.mapper.context

data class ScheduleLessonToEntityMapperContext(
    val scheduleId: Long,
    val lessonId: String,
    val dayId: String,
)
