package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject

@Entity
data class ScheduleDayTable (
    @PrimaryKey(autoGenerate = true) var id: Int = -1,
    @ColumnInfo val date: String,
    @ColumnInfo val dateUnixTime: Long,
    @ColumnInfo val weekDayName: String,
    @ColumnInfo val weekDayNumber: Int,
    @ColumnInfo val weekNumber: Int,
    @ColumnInfo val schedule: ArrayList<GroupScheduleSubjectTable>
) {

    fun toScheduleDay() = ScheduleDay(
        id = id,
        date = date,
        dateUnixTime = dateUnixTime,
        weekDayTitle = weekDayName,
        weekDayNumber = weekDayNumber,
        weekNumber = weekNumber,
        schedule = schedule.map { it.toScheduleSubject() } as ArrayList<ScheduleSubject>
    )

}


