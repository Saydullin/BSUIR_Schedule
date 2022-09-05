package com.bsuir.bsuirschedule.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScheduleDayTable (
    @PrimaryKey(autoGenerate = true) var id: Int = -1,
    @ColumnInfo val date: String,
    @ColumnInfo val weekDayName: String,
    @ColumnInfo val weekDayNumber: Int,
    @ColumnInfo val lessonsAmount: Int,
    @ColumnInfo val schedule: ArrayList<GroupScheduleSubjectTable>
)


