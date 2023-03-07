package com.bsuir.bsuirschedule.domain.models

data class ScheduleLastUpdatedDate (
    val lastUpdateDate: String?
) {

    companion object {
        val empty = ScheduleLastUpdatedDate(
            lastUpdateDate = ""
        )
    }

}