package com.example.bsuirschedule.domain.models

import com.example.bsuirschedule.data.db.entities.CurrentWeekTable

data class CurrentWeek(
    val week: Int,
    val time: Long
) {

    fun toCurrentWeekTable() = CurrentWeekTable(
        week = week,
        time = time
    )

}


