package com.bsuir.bsuirschedule.domain.models

data class ScheduleSubjectDate (
    val id: Int,
    val startMillis: Long,
    val endMillis: Long,
    val startTime: String,
    val endTime: String,
    val breakMinutes: SubjectBreakTime,
    val lessonDate: String,
    val startFullDate: String,
    val endFullDate: String,
)


