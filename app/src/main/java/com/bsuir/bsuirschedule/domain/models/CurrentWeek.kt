package com.bsuir.bsuirschedule.domain.models

import com.bsuir.bsuirschedule.data.db.entities.CurrentWeekTable

data class CurrentWeek(
    val week: Int,
    val time: Long
) {

    fun toCurrentWeekTable() = CurrentWeekTable(
        week = week,
        time = time
    )

}


