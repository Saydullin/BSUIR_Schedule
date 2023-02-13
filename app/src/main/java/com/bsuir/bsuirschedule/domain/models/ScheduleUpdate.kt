package com.bsuir.bsuirschedule.domain.models

data class ScheduleUpdate (
    val dateInMillis: Long,
    val previousSchedule: Schedule,
    val currentSchedule: Schedule,
    val changedScheduleDays: List<ScheduleDay>,
    val changesCounter: Int
) {

}


