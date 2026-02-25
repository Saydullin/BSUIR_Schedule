package com.bsuir.domain.model.schedule.full

data class FullScheduleDay(
    val lessons: List<FullScheduleLesson>?,
    val date: Long,
    val week: Int,
)


