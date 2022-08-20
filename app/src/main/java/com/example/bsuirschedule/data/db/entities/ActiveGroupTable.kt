package com.example.bsuirschedule.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ActiveGroupTable (
    @PrimaryKey val id: Int,
    @ColumnInfo val groupId: Int,
    @ColumnInfo val groupScheduleId: Int
)


