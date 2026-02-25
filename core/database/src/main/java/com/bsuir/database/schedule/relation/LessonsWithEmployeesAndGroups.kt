package com.bsuir.database.schedule.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.bsuir.database.schedule.entity.ScheduleEmployeeEntity
import com.bsuir.database.schedule.entity.ScheduleGroupEntity
import com.bsuir.database.schedule.entity.ScheduleLessonEmployeeEntity
import com.bsuir.database.schedule.entity.ScheduleLessonEntity
import com.bsuir.database.schedule.entity.ScheduleLessonGroupEntity

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
    @Relation(
        parentColumn = "scheduleId",
        entityColumn = "scheduleId"
    )
    val scheduleGroup: ScheduleGroupEntity?,
    @Relation(
        parentColumn = "scheduleId",
        entityColumn = "scheduleId"
    )
    val scheduleEmployee: ScheduleEmployeeEntity?,
)


