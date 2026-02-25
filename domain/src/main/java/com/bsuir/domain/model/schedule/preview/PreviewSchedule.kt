package com.bsuir.domain.model.schedule.preview

import com.bsuir.domain.model.schedule.common.ScheduleEmployee
import com.bsuir.domain.model.schedule.common.ScheduleGroup

data class PreviewSchedule(
    val startDate: String?,
    val endDate: String?,
    val startExamsDate: String?,
    val endExamsDate: String?,
    val employee: ScheduleEmployee?,
    val group: ScheduleGroup?,
    val currentTerm: String?,
    val nextTerm: String?,
    val currentPeriod: String?,
    val partTimeOrRemote: Boolean?,
)