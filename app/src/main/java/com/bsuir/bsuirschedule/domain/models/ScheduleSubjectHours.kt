package com.bsuir.bsuirschedule.domain.models

data class ScheduleSubjectHours(
    val past: Int,
    val total: Int,
) {

    companion object {
        val empty = ScheduleSubjectHours(
            past = 0,
            total = 0
        )
    }

}
