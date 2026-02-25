package com.bsuir.domain.model.schedule.full

import com.bsuir.domain.model.schedule.common.ScheduleEmployee
import com.bsuir.domain.model.schedule.common.ScheduleGroup

data class FullSchedule(
    val startDate: String?,
    val endDate: String?,
    val startExamsDate: String?,
    val endExamsDate: String?,
    val employee: ScheduleEmployee?,
    val group: ScheduleGroup?,
    val schedules: List<FullScheduleDay>?,
    val nextSchedules: List<FullScheduleDay>?,
    val currentTerm: String?,
    val nextTerm: String?,
    val exams: List<FullScheduleDay>?,
    val currentPeriod: String?,
    val partTimeOrRemote: Boolean?,
)


