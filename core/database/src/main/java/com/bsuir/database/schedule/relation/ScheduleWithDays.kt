package com.bsuir.database.schedule.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.bsuir.database.schedule.entity.ScheduleDayEntity
import com.bsuir.database.schedule.entity.ScheduleEntity

data class ScheduleWithDays(
    @Embedded val schedule: ScheduleEntity,
    @Relation(
        entity = ScheduleDayEntity::class,
        parentColumn = "scheduleId",
        entityColumn = "scheduleId"
    )
    val days: List<DaysWithLessons>,
)