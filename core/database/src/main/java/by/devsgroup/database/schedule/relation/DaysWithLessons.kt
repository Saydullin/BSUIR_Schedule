package by.devsgroup.database.schedule.relation

import androidx.room.Embedded
import androidx.room.Relation
import by.devsgroup.database.schedule.entity.ScheduleDayEntity
import by.devsgroup.database.schedule.entity.ScheduleLessonEntity

data class DaysWithLessons(
    @Embedded val day: ScheduleDayEntity,
    @Relation(
        entity = ScheduleLessonEntity::class,
        parentColumn = "dayId",
        entityColumn = "dayId"
    )
    val lessons: List<LessonsWithEmployeesAndGroups>
)


