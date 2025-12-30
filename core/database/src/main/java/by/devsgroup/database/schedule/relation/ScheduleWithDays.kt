package by.devsgroup.database.schedule.relation

import androidx.room.Embedded
import androidx.room.Relation
import by.devsgroup.database.schedule.entity.ScheduleDayEntity
import by.devsgroup.database.schedule.entity.ScheduleEntity

data class ScheduleWithDays(
    @Embedded val schedule: ScheduleEntity,
    @Relation(
        entity = ScheduleDayEntity::class,
        parentColumn = "scheduleId",
        entityColumn = "scheduleId"
    )
    val days: List<DaysWithLessons>,
)