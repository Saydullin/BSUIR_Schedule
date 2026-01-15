package by.devsgroup.database.schedule.relation

import androidx.room.Embedded
import androidx.room.Relation
import by.devsgroup.database.schedule.entity.ScheduleLessonEmployeeEntity
import by.devsgroup.database.schedule.entity.ScheduleLessonEntity
import by.devsgroup.database.schedule.entity.ScheduleLessonGroupEntity

data class LessonsWithEmployeesAndGroups(
    @Embedded val lesson: ScheduleLessonEntity,
    @Relation(
        parentColumn = "lessonId",
        entityColumn = "lessonId",
    )
    val employees: List<ScheduleLessonEmployeeEntity>,
    @Relation(
        parentColumn = "lessonId",
        entityColumn = "lessonId",
    )
    val groups: List<ScheduleLessonGroupEntity>,
)