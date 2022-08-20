package com.example.bsuirschedule.domain.models

data class GroupScheduleDB (
    val id: Int,
    val startDate: String,
    val endDate: String,
    val startExamsDate: String,
    val endExamsDate: String,
    val studentGroupDto: Int,
    val todayDate: String,
    val tomorrowDate: String,
    val schedules: Int
)


