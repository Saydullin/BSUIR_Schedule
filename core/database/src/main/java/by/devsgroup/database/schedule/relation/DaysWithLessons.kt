package by.devsgroup.database.schedule.relation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import by.devsgroup.database.schedule.entity.ScheduleDayEntity
import by.devsgroup.database.schedule.entity.ScheduleEntity
import by.devsgroup.database.schedule.entity.ScheduleLessonEntity

data class DaysWithLessons(
    @Embedded val day: ScheduleDayEntity,
    @Relation(
        parentColumn = "dayId",
        entityColumn = "dayId"
    )
    val lessons: List<ScheduleLessonEntity>
)


